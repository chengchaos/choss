package luxe.chaos.choss.vfs.service;

import luxe.chaos.choss.vfs.business.ChossObject;
import luxe.chaos.choss.vfs.business.ChossObjectSummary;
import luxe.chaos.choss.vfs.business.ObjectListResult;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * <strong>
 * 用一句话描述功能
 * </strong><br /><br />
 * 如题
 * </p>
 *
 * @author chengchao - 2020/4/13 17:41 <br />
 * @see [相关类方法]
 * @since [产品模块版本]
 */
public interface ChossStore {


    boolean createBucketStore(String bucketName) throws IOException;

    boolean deleteBucketStore(String bucketName) throws IOException;

    boolean createSeqTable() throws IOException;

    boolean put(String bucketName,
                String key,
                ByteBuffer content,
                long length,
                String mediaType,
                Map<String, String> properties) throws Exception;

    ChossObjectSummary getSummary(String bucketName, String rowKey) throws IOException;

    List<ChossObjectSummary> getSummarys(String bucketName, String beginKey, String endKey) throws IOException;

    ObjectListResult listDir(String bucketName, String dir, String beginKey, int maxCount) throws IOException;

    ObjectListResult listByPrefix(String bucketName, String dir, String beginKey, String prefix, int maxCount) throws  IOException;

    ChossObject getObject(String bucketName, String rowKey) throws IOException;

    boolean deleteObject(String bucketName, String rowkey) throws IOException;


}
