package luxe.chaos.choss.store.service.impl;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import luxe.chaos.choss.store.beans.*;
import luxe.chaos.choss.store.helper.HBaseHelper;
import luxe.chaos.choss.store.service.HBaseService;
import luxe.chaos.choss.store.service.HdfsService;
import luxe.chaos.choss.store.service.StoreService;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.directory.shared.kerberos.codec.transitedEncoding.actions.StoreContents;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.convert.StringToRedisClientInfoConverter;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class StoreServiceImpl implements StoreService {

    private static final Logger LOGGER = LoggerFactory.getLogger(StoreServiceImpl.class);

    @Autowired
    HdfsService hdfsService;

    @Autowired
    HBaseService hBaseService;

    private final ObjectMapper mapper = new ObjectMapper();

    private CuratorFramework zookeeperClient;


    @PostConstruct
    public void execPostConstruct() {

        String zkUrls = "192.168.0.111:2181";
        zookeeperClient = CuratorFrameworkFactory.newClient(zkUrls,
                new ExponentialBackoffRetry(20, 5));
        this.zookeeperClient.start();
    }

    /**
     * @throws IOException
     */
    @Override
    public void createSeqTable() throws IOException {
        hBaseService.createTable(StoreConstants.TABLE_NAME_BUCKET_DIR_SEQ,
                new String[]{StoreConstants.CF_NAME_BUCKET_DIR_SEQ},
                null);
    }

    /**
     * @throws IOException
     */
    public void createSeqTableIfAbsent() throws IOException {
        if (!hBaseService.existsTable(StoreConstants.TABLE_NAME_BUCKET_DIR_SEQ)) {
            createSeqTable();
        }
    }


    @Override
    public void createBucketStore(String bucketId) throws IOException {

        // 1 创建目录表
        hBaseService.createTable(StoreConstants.getDirTableName(bucketId),
                StoreConstants.getColumnFamilyForDir(),
                null);

        // 2 创建文件表
        hBaseService.createTable(StoreConstants.getObjTableName(bucketId),
                StoreConstants.getColumnFamilyForoObj(),
                StoreConstants.OBJ_REGIONS);


        this.createSeqTableIfAbsent();

        // 3 添加到 seq 表
        Put put = new Put(bucketId.getBytes(StandardCharsets.UTF_8));
        put.addColumn(StoreConstants.CF_NAME_BUCKET_DIR_SEQ.getBytes(),
                StoreConstants.QUALIFIER_BUCKET_SEQ_ID.getBytes(),
                Bytes.toBytes(0L));
        hBaseService.putRow(StoreConstants.TABLE_NAME_BUCKET_DIR_SEQ, put);

        // 4 创建 HDFS 目录
        hdfsService.mkDirs(StoreConstants.FILE_STORE_ROOT + "/" + bucketId);

    }

    /**
     * @param bucketId bucketId
     * @throws IOException
     */
    @Override
    public void deleteBucketStore(String bucketId) throws IOException {

        // 1：删除目录表和文件表
        hBaseService.deleteTable(StoreConstants.getDirTableName(bucketId));
        hBaseService.deleteTable(StoreConstants.getObjTableName(bucketId));

        // 2：删除 seq 表中的记录
        hBaseService.deleteRow(StoreConstants.TABLE_NAME_BUCKET_DIR_SEQ, bucketId);

        // 3：删除 HDFS 上的目录
        hdfsService.rmDirs(StoreConstants.FILE_STORE_ROOT + "/" + bucketId);

    }


    private boolean dirExist(String bucketId, String rowKey) {
        String tableName = StoreConstants.getDirTableName(bucketId);
        boolean exists = this.hBaseService.existsRow(tableName, rowKey);
        LOGGER.debug("table => {}, exists => {}", tableName, exists);
        return exists;
    }

    private String getDirSeqId(String bucketId, String rowKey) {
        String tableName = StoreConstants.getDirTableName(bucketId);
        Optional<Result> resultOptional = this.hBaseService.getResult(tableName, rowKey);
        if (resultOptional.isPresent()) {
            Result result = resultOptional.get();
            String seqId = Bytes.toString(result.getValue(Bytes.toBytes(StoreConstants.CF_NAME_DIR_META),
                    Bytes.toBytes(StoreConstants.QUALIFIER_DIR_SEQ_ID)));

            LOGGER.debug("seqId => {}", seqId);
            return seqId;
        }

        throw new NullPointerException("Get Dir's Sequence ID, bug got null");
    }

    private String makeDirSeqId(String bucketId) {
        long value = this.hBaseService.incrementColumnValue(StoreConstants.TABLE_NAME_BUCKET_DIR_SEQ,
                bucketId,
                StoreConstants.CF_NAME_BUCKET_DIR_SEQ,
                StoreConstants.QUALIFIER_BUCKET_SEQ_ID,
                1);
        String seqId = new DecimalFormat("0000000000").format(value);
        LOGGER.debug("Make Dir Sequence ID => {}", seqId);
        return seqId;
    }

    /**
     * @param bucketId
     * @param rowKey
     * @return
     */
    private String putDir(String bucketId, String rowKey) {

        if (StringUtils.isEmpty(rowKey) ||
                !rowKey.startsWith("/") || !rowKey.endsWith("/")) {
            throw new IllegalArgumentException("rowKey must be a directory, but it's => " + rowKey);
        }

        if (dirExist(bucketId, rowKey)) {
            return getDirSeqId(bucketId, rowKey);
        }

        InterProcessMutex lock = null;
        String tableName = StoreConstants.getDirTableName(bucketId);

        // 1: 从 ZK 获取锁
        try {
            String lockey = rowKey.replace("/", "_");
            lock = new InterProcessMutex(zookeeperClient,
                    StoreConstants.FILE_STORE_ROOT + "/" + bucketId + "/" + lockey);
            lock.acquire(10, TimeUnit.SECONDS);

            // 递归边界
            if (rowKey.equals("/")) {
                // 创建 根 目录
                String seqId = makeDirSeqId(bucketId);
                Put put = HBaseHelper.newPut(rowKey,
                        StoreConstants.CF_NAME_DIR_META,
                        StoreConstants.QUALIFIER_DIR_SEQ_ID,
                        seqId);
                hBaseService.putRow(tableName, put);
                return seqId;
            }

            // From "/root/abc/def/" to "/root/abc/def"
            String fallPathDir = rowKey.substring(0, rowKey.lastIndexOf("/"));
            // From "/root/abc/def/" to "def/"
            String lastDirName = rowKey.substring(fallPathDir.lastIndexOf("/") + 1);
            LOGGER.debug("rowKey => {}, dir => {}, name => {}", rowKey, fallPathDir, lastDirName);


            if (lastDirName.length() > 0) {
                // From "/root/abc/def" to "/root/abc/"
                String parent = fallPathDir.substring(0, fallPathDir.lastIndexOf("/") + 1);
                LOGGER.debug("parent => {}", parent);
                if (!dirExist(bucketId, parent)) {
                    this.putDir(bucketId, parent);
                }
                // 在父目录的 sub  列族内添加 自己
                Put put = HBaseHelper.newPut(parent,
                        StoreConstants.CF_NAME_DIR_SUBS, lastDirName, "1");
                hBaseService.putRow(tableName, put);

                // 在去添加到目录表。
                String seqId = makeDirSeqId(bucketId);

                // 创建自己的目录
                put = HBaseHelper.newPut(fallPathDir + "/",
                        StoreConstants.CF_NAME_DIR_META,
                        StoreConstants.QUALIFIER_DIR_SEQ_ID,
                        seqId
                );
                hBaseService.putRow(tableName, put);
                return seqId;
            }
        } catch (Exception e) {
            LOGGER.error("Exception ", e);
        } finally {
            // 3： 释放锁
            if (lock != null) {
                try {
                    lock.release();
                } catch (Exception e) {
                    LOGGER.error("Release zk lock ... but ", e);
                }
            }
        }

        return null;
    }

    private void putFile(String bucketId, String rowKey, String seqId,
                         ByteBuffer content,
                         long length, String mediaType,
                         Map<String, String> props) {
        // 获取锁
        InterProcessMutex lock = null;
        String lockey = rowKey.replace("/", "_");
        try {
            lock = new InterProcessMutex(zookeeperClient,
                    StoreConstants.FILE_STORE_ROOT + "/" + bucketId + "/" + lockey);
            lock.acquire(10, TimeUnit.SECONDS);
            // 上传文件
            String fileKey = seqId + "_" + rowKey.substring(rowKey.lastIndexOf("/") + 1);

            Put contentPut = new Put(Bytes.toBytes(fileKey));

            if (!Strings.isNullOrEmpty(mediaType)) {
                contentPut.addColumn(
                        Bytes.toBytes(StoreConstants.CF_NAME_OBJ_META),
                        Bytes.toBytes(StoreConstants.QUALIFIER_OBJ_MEDIA_TYPE),
                        Bytes.toBytes(mediaType));
            }

            if (!CollectionUtils.isEmpty(props)) {
                String json = mapper.writeValueAsString(props);
                contentPut.addColumn(
                        Bytes.toBytes(StoreConstants.CF_NAME_OBJ_META),
                        Bytes.toBytes(StoreConstants.QUALIFIER_OBJ_PROPS),
                        Bytes.toBytes(json));
            }
            if (length > 0) {
                contentPut.addColumn(
                        Bytes.toBytes(StoreConstants.CF_NAME_OBJ_META),
                        Bytes.toBytes(StoreConstants.QUALIFIER_OBJ_LEN),
                        Bytes.toBytes(length));
            }
            // 判断文件大侠，小于 20 兆，存 HBase
            if (length <= StoreConstants.FILE_STORE_THRESHOLD) {
//                ByteBuffer byteBuffer = ByteBuffer.wrap(StoreConstants.QUALIFIER_OBJ_CONTENT.getBytes());
//                contentPut.addColumn(
//                        Bytes.toBytes(StoreConstants.CF_NAME_OBJ_CONT),
//                        byteBuffer,
//                        System.currentTimeMillis(),
//                        content);
//                byteBuffer.clear();
                contentPut.addColumn(
                        Bytes.toBytes(StoreConstants.CF_NAME_OBJ_CONT),
                        Bytes.toBytes(StoreConstants.QUALIFIER_OBJ_CONTENT),
                        System.currentTimeMillis(),
                        content.array());
            } else {
                String fileDir = StoreConstants.FILE_STORE_ROOT + "/" +
                        bucketId + "/" + seqId;
                String name = rowKey.substring(rowKey.lastIndexOf("/") + 1);
//                InputStream inputStream = new ByteBufferInputStream(content);

                try (InputStream inputStream = new ByteArrayInputStream(content.array())) {
                    this.hdfsService.saveFile(fileDir, name, inputStream, length, (short) 1);
                }
            }

            this.hBaseService.putRow(StoreConstants.getObjTableName(bucketId), contentPut);

        } catch (Exception e) {
            LOGGER.error("Exception ", e);
        } finally {
            // 释放锁
            if (lock != null) {
                try {
                    lock.release();
                } catch (Exception e) {
                    LOGGER.error("Release zk lock ... but ", e);
                }
            }
        }
    }

    @Override
    public void putObject(String bucketId, String rowKey, ByteBuffer content,
                          long length, String mediaType, Map<String, String> props) throws IOException {

        LOGGER.debug("rowKey => {}", rowKey);
        // 是否是创建按目录
        // if the operate is just create DIR , then create this dir and return.
        if (rowKey.endsWith("/")) {
            putDir(bucketId, rowKey);
            return;
        }

        // 1：获取 seqid
        // Get SeqId
        String dir = rowKey.substring(0, rowKey.lastIndexOf("/") + 1);
        LOGGER.debug("dir => {}", dir);

        String seqId = null;
        int whileLoop = 0;
        while (seqId == null) {
            whileLoop += 1;
            if (!dirExist(bucketId, dir)) {
                // 如果目录不存在， 则创建目录
                // if the dir not exist, execute create dir .
                seqId = putDir(bucketId, dir);
            } else {
                // 如果目录存在，返回此目录的 SqqId
                // if the dir exist, return dir's seqId.
                seqId = getDirSeqId(bucketId, dir);
            }
            LOGGER.debug("whileLoop => {}, seqId => {}", whileLoop, seqId);
            if (whileLoop > 100) {
                break;
            }
        }

        if (seqId == null) {
            throw new NullPointerException("Cann't got Sequence ID, it is null yet!");
        }
        // 2：上传文件
        this.putFile(bucketId, rowKey, seqId,
                content, length, mediaType, props);


    }

    private StoreObjectSummary dirInfo2StoreObjectSummary(String bucketId, String rowKey, Result result) {

        StoreObjectSummary summary = new StoreObjectSummary();
        summary.setId(Bytes.toString(result.getRow()));
        summary.setAttrs(Collections.emptyMap());
        summary.setBucket(bucketId);
        summary.setKey(rowKey);
        summary.setMediaType(StringUtils.EMPTY);
        summary.setLength(0L);
        summary.setName(rowKey);

        summary.setLastModifyTime(result.rawCells()[0].getTimestamp());

        return summary;
    }

    private StoreObjectSummary objInfo2StoreObjectSummary(String bucketId, String dirName, Result result) {

        String id = Bytes.toString(result.getRow());
        String name = id.split("_", 2)[1];
        long timestamp = result.rawCells()[0].getTimestamp();

        StoreObjectSummary summary = new StoreObjectSummary();

        summary.setId(id);
        summary.setName(name);
        summary.setKey(dirName + name);
        summary.setLastModifyTime(timestamp);

        summary.setBucket(bucketId);


        summary.setAttrs(Collections.emptyMap());
        summary.setLength(Bytes.toLong(result.getValue(
                Bytes.toBytes(StoreConstants.CF_NAME_OBJ_META),
                Bytes.toBytes(StoreConstants.QUALIFIER_OBJ_LEN)
        )));

        summary.setMediaType(Bytes.toString(result.getValue(
                Bytes.toBytes(StoreConstants.CF_NAME_OBJ_META),
                Bytes.toBytes(StoreConstants.QUALIFIER_OBJ_MEDIA_TYPE)
        )));


        return summary;
    }

    @Override
    public StoreObjectSummary getSummary(String bucketId, String fileName) throws IOException {
        // 1: 判断释放是目录
        boolean isFolder = fileName.endsWith("/");

        // 2： 如果是目录， 获取目录的 Summary 属性
        if (isFolder) {
            Optional<Result> resultOptional = this.hBaseService.getResult(
                    StoreConstants.getDirTableName(bucketId),
                    fileName);
            // 读取目录的基础属性，转换成 StoreObjectSummary 对象
            return resultOptional
                    .map(result -> dirInfo2StoreObjectSummary(bucketId, fileName, result))
                    .orElse(null);

        }

        // 3： 如果不是目录，获取文件的 Summary 属性。
        // 先获取父目录
        final String dirRowKey = fileName.substring(0, fileName.lastIndexOf("/") + 1);
        String seqId = this.getDirSeqId(bucketId, dirRowKey);
        if (seqId == null) {
            LOGGER.warn("Sequence ID is null, Why? ");
            return null;
        }

        String fileRowKey = seqId + "_" + fileName.substring(fileName.lastIndexOf("/") + 1);
        Optional<Result> resultOptional = hBaseService.getResult(StoreConstants.getObjTableName(bucketId),
                fileRowKey);

        // 读取目录的基础属性，转换成 StoreObjectSummary 对象
        return resultOptional
                .map(result -> objInfo2StoreObjectSummary(bucketId, dirRowKey, result))
                .orElse(null);

    }

    @Override
    public StoreObject getObject(String bucketId, String fileName) throws IOException {
        // 1: 判断是否是目录
        boolean isFolder = fileName.endsWith("/");

        // 2： 如果是目录， 获取目录的 Summary 属性
        if (isFolder) {
            Optional<Result> resultOptional = this.hBaseService.getResult(
                    StoreConstants.getDirTableName(bucketId),
                    fileName);
            if (!resultOptional.isPresent()) {
                return null;
            }
            Result result = resultOptional.get();
            ObjectMetaData objectMetaData = new ObjectMetaData();
            objectMetaData.setBucketId(bucketId);
            objectMetaData.setKey(fileName);
            objectMetaData.setLength(0L);
            objectMetaData.setLength(result.rawCells()[0].getTimestamp());

            StoreObject storeObject = new StoreObject();
            storeObject.setObjectMetaData(objectMetaData);

            return storeObject;
        }
        String dirRowKey = fileName.substring(0, fileName.lastIndexOf("/") + 1);
        String seqId = this.getDirSeqId(bucketId, dirRowKey);
        if (seqId == null) {
            LOGGER.warn("Sequence ID is null, Why? ");
            return null;
        }

        String fileRowKey = seqId + "_" + fileName.substring(fileName.lastIndexOf("/") + 1);
        Optional<Result> resultOptional = hBaseService.getResult(StoreConstants.getObjTableName(bucketId),
                fileRowKey);
        if (!resultOptional.isPresent()) {
            return null;
        }

        Result result = resultOptional.get();
        ObjectMetaData objectMetaData = new ObjectMetaData();
        objectMetaData.setBucketId(bucketId);
        objectMetaData.setKey(fileName);
        objectMetaData.setLength(0L);
        objectMetaData.setLength(result.rawCells()[0].getTimestamp());

        StoreObject storeObject = new StoreObject();
        storeObject.setObjectMetaData(objectMetaData);

        // 读取文件内容
        if (result.containsNonEmptyColumn(
                Bytes.toBytes(StoreConstants.CF_NAME_OBJ_CONT),
                Bytes.toBytes(StoreConstants.QUALIFIER_OBJ_CONTENT))) {

            ByteArrayInputStream inputStream = new ByteArrayInputStream(result.getValue(
                    Bytes.toBytes(StoreConstants.CF_NAME_OBJ_CONT),
                    Bytes.toBytes(StoreConstants.QUALIFIER_OBJ_CONTENT)));
            storeObject.setInputStream(inputStream);
        } else {

            String fileDir = StoreConstants.FILE_STORE_ROOT + "/" +
                    bucketId + "/" + seqId;
            InputStream inputStream = hdfsService.openFile(fileDir,
                    fileName.substring(fileName.lastIndexOf("/")));
            storeObject.setInputStream(inputStream);
        }
        return storeObject;

    }

    @Override
    public List<StoreObjectSummary> listSummaries(String bucketId, String startKey, String endKey) throws IOException {

        return null;
    }

    @Override
    public ObjectListResult listDir(String bucketId, String dir, String start, int maxCount) throws IOException {
        return null;
    }

    @Override
    public ObjectListResult listByPrefix(String bucketId, String dir, String start, String prefix, int maxCount) throws IOException {
        return null;
    }

    @Override
    public void deleteObject(String bucketId, String fileName) throws IOException {

        // 1: 判断是否是目录
        boolean isFolder = fileName.endsWith("/");

        // 删除目录
        if (isFolder) {
            // 判断这个目录里面是否有文件， 有则删除这些文件。

            // 判断这个目录下面是否又子目录。

            // 有， 则删除子目录

            // 删除父目录的 sub 子项
            String tempFileName = fileName.substring(0, fileName.lastIndexOf("/"));
            String parentDir = fileName.substring(0, tempFileName.lastIndexOf("/") + 1);
            String dirName = fileName.substring(tempFileName.lastIndexOf("/") + 1);

            LOGGER.debug("fileName => {}, tempFileName => {}, parentDir => {}, dirName ={}",
                    fileName, tempFileName, parentDir, dirName             );

            boolean deleteParentSubs = this.hBaseService.deleteQualifier(
                    StoreConstants.getDirTableName(bucketId),
                    parentDir,
                    StoreConstants.CF_NAME_DIR_SUBS,
                    dirName);
            LOGGER.debug("deleteParentSubs => {}", deleteParentSubs);
            // 删除自己

            boolean deleteDirTable = this.hBaseService.deleteRow(StoreConstants.getDirTableName(bucketId),
                    fileName);
            LOGGER.debug("deleteDirTable ({}) => {}", fileName, deleteDirTable);

            return;
        }

        // 删除文件

        // 删除 Hbase 中的文件

        // 删除 HDFS 上的文件。


    }
}
