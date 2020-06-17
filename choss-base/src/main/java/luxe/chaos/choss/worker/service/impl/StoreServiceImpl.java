package luxe.chaos.choss.worker.service.impl;

import luxe.chaos.choss.worker.beans.ObjectListResult;
import luxe.chaos.choss.worker.beans.StoreObjectSummary;
import luxe.chaos.choss.worker.service.HdfsService;
import luxe.chaos.choss.worker.service.StoreService;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@Service
public class StoreServiceImpl implements StoreService {


    private static final Logger LOGGER = LoggerFactory.getLogger(StoreServiceImpl.class);

    private Connection connection = null;

    private HdfsService hdfsService;

    private String zkUrls;

    private CuratorFramework curatorFramework;

    public StoreServiceImpl(Connection connection,
                            HdfsService hdfsService,
                            String zkUrls) {
        this.connection = connection;
        this.hdfsService = hdfsService;
        this.zkUrls = zkUrls;
        curatorFramework = CuratorFrameworkFactory.newClient(zkUrls,
                new ExponentialBackoffRetry(20, 5));
        this.curatorFramework.start();
    }

    public static final byte[][] OBJ_REGIONS = new byte[][] {
            Bytes.toBytes("1"),
            Bytes.toBytes("4"),
            Bytes.toBytes("7")
    };

    @Override
    public void createBucketStore(String bucketId) throws IOException {

        // 1 创建目录表
        HBaseHelper.createTable(connection, StoreHelper.getDirTableName(bucketId),
                StoreHelper.getDirColumnFamily(), null);

        // 2 创建文件表
        HBaseHelper.createTable(connection, StoreHelper.getObjTableName(bucketId),
                StoreHelper.getObjColumnFamily(), OBJ_REGIONS);

        // 3 添加到 seq 表

        Put put = new Put(bucketId.getBytes(StandardCharsets.UTF_8));
        put.addColumn(StoreHelper.BUCKET_DIR_SEQ_CF_BYTES,
                StoreHelper.BUCKET_DIR_SEQ_QUALIFIER,
                Bytes.toBytes(0L));


        HBaseHelper.putRow(connection, StoreHelper.BUCKET_DIR_SEQ_TABLE, put);
        // 4 创建 HDFS 目录

        hdfsService.mkDir(StoreHelper.FILE_STORE_ROOT + "/" + bucketId);

    }

    @Override
    public void deleteBucketStore(String bucketId) throws IOException {

        // 1：删除目录表和文件表
        HBaseHelper.deleteTable(connection, StoreHelper.getDirTableName(bucketId));
        HBaseHelper.deleteTable(connection, StoreHelper.getObjTableName(bucketId));
        // 2：删除 seq 表中的记录
        HBaseHelper.deleteRow(connection, StoreHelper.BUCKET_DIR_SEQ_TABLE, bucketId);

        // 3：删除 HDFS 上的目录
        hdfsService.deleteDir(StoreHelper.FILE_STORE_ROOT + "/" + bucketId);


    }

    @Override
    public void createSeqTable() throws IOException {
        HBaseHelper.createTable(connection,
                StoreHelper.BUCKET_DIR_SEQ_TABLE,
                new String[] { StoreHelper.BUCKET_DIR_SEQ_CF },
                null);
    }

    @Override
    public void pub(String bucket, String key, ByteBuffer content, long length, String mediaType, Map<String, String> props) throws IOException {

        // 是否是创建按目录
        if (key.endsWith("/")) {
            pubDir(bucketId, key);
            return;
        }

        // 1：获取 seqid
        String dir = key.substring(0, key.lastIndexOf("/") + 1);
        String hash = null;
        while (hash == null) {
            if (dirExist(bucketId, dir)) {
                hash = putDir(bucketId, dir);
            } else {
                hash = getDirSeqId(bucketId, dir);
            }
        }

        // 2：上传文件
    }

    @Override
    public StoreObjectSummary getSummary(String bucket, String key) throws IOException {
        return null;
    }

    @Override
    public List<StoreObjectSummary> listSummaries(String bucket, String startKey, String endKey) throws IOException {
        return null;
    }

    @Override
    public ObjectListResult listDir(String bucket, String dir, String start, int maxCount) throws IOException {
        return null;
    }

    @Override
    public ObjectListResult listByPrefix(String bucket, String dir, String start, String prefix, int maxCount) throws IOException {
        return null;
    }

    @Override
    public void deleteObject(String bucket, String key) throws IOException {

    }
}
