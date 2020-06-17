package luxe.chaos.choss.vfs.service.impl;

import luxe.chaos.choss.vfs.business.ChossObject;
import luxe.chaos.choss.vfs.business.ChossObjectSummary;
import luxe.chaos.choss.vfs.business.ObjectListResult;
import luxe.chaos.choss.vfs.config.Constants;
import luxe.chaos.choss.vfs.service.ChossStore;
import luxe.chaos.choss.vfs.service.HBaseService;
import luxe.chaos.choss.vfs.service.HdfsService;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;
import org.assertj.core.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * <p>
 * <strong>
 * 用一句话描述功能
 * </strong><br /><br />
 * 如题
 * </p>
 *
 * @author chengchao - 2020/4/13 17:47 <br />
 * @see [相关类方法]
 * @since [产品模块版本]
 */
public class ChossStoreImpl implements ChossStore {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChossStoreImpl.class);

    private Connection connection;

    private HdfsService hdfsService;
    private HBaseService hBaseService;

    private String zookeeperUrls;
    private CuratorFramework zookeeperClient;

    public ChossStoreImpl(Connection connection,
                          HdfsService hdfsService,
                          String zookeeperUrls) {

        this.connection = connection;
        this.hdfsService = hdfsService;
        this.zookeeperUrls = zookeeperUrls;

        this.zookeeperClient = CuratorFrameworkFactory.newClient(zookeeperUrls,
                new ExponentialBackoffRetry(20, 5));

    }


    @Override
    public boolean createBucketStore(String bucketName) throws IOException {
        // 1:  创建目录表
        hBaseService.createTable(Constants.getTableNameOfDir(bucketName),
                Constants.getColumnFamilysOfDir(),
                null);
        // 2： 创建文件表
        hBaseService.createTable(Constants.getTableNameOfObj(bucketName),
                Constants.getColumnFamilyOfObj(),
                Constants.getObjReginos());

        // 3： 添加到 seq 表
        Put put = new Put(Bytes.toBytes(bucketName));
        put.addColumn(Constants.getCfBucketDirSeqBytes(),
                Constants.getQualifierBucketDirSeqBytes(),
                Bytes.toBytes(0L));
        this.hBaseService.putRow(Constants.TABLE_BUCKET_DIR_SEQ, put);

        // 4： 创建 hdfs 目录
        hdfsService.mkdirs(Constants.FILE_STORE_ROOT, bucketName);
        return true;
    }

    @Override
    public boolean deleteBucketStore(String bucketName) throws IOException {
        // 1： 删除目录表和文件表
        hBaseService.deleteTable(Constants.getTableNameOfObj(bucketName));
        hBaseService.deleteTable(Constants.getTableNameOfDir(bucketName));
        // 2： 删除 seq 表中的记录
        hBaseService.deleteRow(Constants.TABLE_BUCKET_DIR_SEQ, bucketName);

        // 3： 删除 hdfs 上的目录
        hdfsService.deleteDir(new String[]{Constants.FILE_STORE_ROOT, bucketName});
        return true;
    }

    @Override
    public boolean createSeqTable() throws IOException {
        this.hBaseService.createTable(Constants.TABLE_BUCKET_DIR_SEQ,
                new String[]{Constants.CF_BUCKET_DIR_SEQ},
                null);
        return true;
    }

    private boolean dirExists(String bucketName, String key) {
//        return hBaseService.existsRow(Constants.getTableNameOfDir(bucketName), key);
        return false;

    }

    private String getDirSeqId(String bucketName, String key) {
        Result result = hBaseService.retrieveRow(Constants.getTableNameOfDir(bucketName), key);
        if (Objects.isNull(result) || result.isEmpty()) {
            return null;
        } else {
            byte[] bytes = result.getValue(
                    Bytes.toBytes(Constants.CF_DIR_META),
                    Bytes.toBytes(Constants.QUALIFIER_DIR_SEQ_ID));

            return Bytes.toString(bytes);
        }
    }

    private void releaseLock(InterProcessMutex lock) throws Exception {
        if (Objects.nonNull(lock)) {
            lock.release();
        }
    }

    private String putDir(String bucketName, String key) throws Exception {
        if (dirExists(bucketName, key)) {
            return null;
        }
        // 从 zookeeper 获取锁
        InterProcessMutex lock = null;

        try {
            String lockey = key.replaceAll("/", "_");
            lock = new InterProcessMutex(zookeeperClient,
                    "/choss" + bucketName + "/" + lockey);
            lock.acquire();

            String dir1 = key.substring(0, key.lastIndexOf("/"));
            String name1 = dir1.substring(dir1.lastIndexOf("/"));

            if (name1.length() > 0) {
                String parent = dir1.substring(0, dir1.lastIndexOf("/") + 1);
                if (!dirExists(bucketName, parent)) {
                    this.putDir(bucketName, parent);
                }
                // 在父目录添加 sub 列族内，添加子项
                Put put = new Put(Bytes.toBytes(parent));
                put.addColumn(Bytes.toBytes(Constants.CF_DIR_SUBS),
                        Bytes.toBytes(name1),
                        Bytes.toBytes("1"));
                hBaseService.putRow(Constants.getTableNameOfDir(bucketName), put);
            }

            // 创建目录

            String seqId = getDirSeqId(bucketName, key);
            String hash = (Objects.isNull(seqId) ? makeDirSeqId(bucketName) : seqId);
            Put dirPut = new Put(Bytes.toBytes(key));
            dirPut.addColumn(Bytes.toBytes(Constants.CF_DIR_META),
                    Bytes.toBytes(Constants.QUALIFIER_DIR_SEQ_ID),
                    Bytes.toBytes(hash));
            hBaseService.putRow(Constants.getTableNameOfDir(bucketName), dirPut);
            return seqId;
        } finally {
            // 释放锁
            releaseLock(lock);
        }
    }


    private String makeDirSeqId(String bucketName) {

        long v = hBaseService.incrementColumnValue(Constants.getTableNameOfDir(bucketName),
                bucketName,
                Constants.CF_BUCKET_DIR_SEQ,
                Constants.QUALIFIER_BUCKET_DIR_SEQ,
                1L);

        return String.format("%da%d", v % 64, v);
    }


    @Override
    public boolean put(String bucketName, String key,
                       ByteBuffer content, long length,
                       String mediaType, Map<String, String> properties) throws Exception {
        // 判断是否是创建目录

        if (key.endsWith("/")) {
            putDir(bucketName, key);
            return true;
        }
        // 获取 seqId
        String dir = key.substring(0, key.lastIndexOf("/") + 1);
        String hash = null;
        while (hash == null) {
            if (dirExists(bucketName, dir)) {
                hash = putDir(bucketName, dir);
            } else {
                hash = getDirSeqId(bucketName, dir);
            }
        }
        // 上传文件到文件表。

        InterProcessMutex lock = null;
        String lockey = key.replaceAll("/", "_");
        lock = new InterProcessMutex(zookeeperClient, "/choss/" + bucketName + "/" + lockey);
        lock.acquire();

        // file 的 rowKey
        String fileKey = hash + "_" + key.substring(key.lastIndexOf("/") + 1);
        Put contentPut = new Put(Bytes.toBytes(fileKey));

        if (!Strings.isNullOrEmpty(mediaType)) {
            contentPut.addColumn(
                    Bytes.toBytes(Constants.CF_OBJ_META),
                    Bytes.toBytes(Constants.QUALIFIER_OBJ_MEDIA_TYPE),
                    Bytes.toBytes(mediaType));
        }
        // todo add props length

        // 判断文件的大小， 小于 20M 存到 HBase
        // 否则存到 HDFS
        if (length <= Constants.FILE_STORE_THRESHOLD) {
            ByteBuffer byteBuffer = ByteBuffer.wrap(Bytes.toBytes(Constants.QUALIFIER_OBJ_CONT));
            contentPut.addColumn(
                    Bytes.toBytes(Constants.CF_OBJ_CONT),
                    Bytes.toBytes(Constants.QUALIFIER_OBJ_MEDIA_TYPE),
                    Bytes.toBytes(mediaType));
        } else {

//            hdfsService.saveFile(Constants.FILE_STORE_ROOT, )
        }

        return true;
    }

    @Override
    public ChossObjectSummary getSummary(String bucketName, String rowKey) throws IOException {
        return null;
    }

    @Override
    public List<ChossObjectSummary> getSummarys(String bucketName, String beginKey, String endKey) throws IOException {
        return null;
    }

    @Override
    public ObjectListResult listDir(String bucketName, String dir, String beginKey, int maxCount) throws IOException {
        return null;
    }

    @Override
    public ObjectListResult listByPrefix(String bucketName, String dir, String beginKey, String prefix, int maxCount) throws IOException {
        return null;
    }

    @Override
    public ChossObject getObject(String bucketName, String rowKey) throws IOException {
        return null;
    }

    @Override
    public boolean deleteObject(String bucketName, String rowkey) throws IOException {
        return false;
    }
}
