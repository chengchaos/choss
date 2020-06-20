package luxe.chaos.choss.store.service;


import luxe.chaos.choss.store.beans.ObjectListResult;
import luxe.chaos.choss.store.beans.StoreObject;
import luxe.chaos.choss.store.beans.StoreObjectSummary;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;

public interface StoreService {

    void createBucketStore(String bucketId) throws IOException;

    void deleteBucketStore(String bucketId) throws IOException;

    void createSeqTable() throws IOException;

    /**
     *
     * @param bucketId 其实就是表名称 （username_bucketname)
     * @param rowKey rowkey 的形式是目录的全路径 (/dir1/dir2/dir3/)
     * @param content 文件的内容
     * @param length 文件的大小
     * @param mediaType 媒体类型
     * @param props 其他属性
     * @throws IOException
     */
    void putObject(String bucketId, String rowKey, ByteBuffer content,
                   long length, String mediaType, Map<String, String> props) throws IOException;

    StoreObjectSummary getSummary(String bucketId, String fileName) throws IOException;

    StoreObject getObject(String bucketId, String fileName) throws IOException;

    List<StoreObjectSummary> listSummaries(String bucketId, String startKey, String endKey) throws IOException;

    ObjectListResult listDir(String bucketId, String dir, String start, int maxCount) throws IOException;

    ObjectListResult listByPrefix(String bucketId, String dir, String start, String prefix, int maxCount) throws IOException;

    void deleteObject(String bucketId, String fileName) throws IOException;




}
