package luxe.chaos.choss.sdk;

import luxe.chaos.choss.common.entity.ChossObjectSummary;
import luxe.chaos.choss.common.entity.PutRequest;
import luxe.chaos.choss.common.model.BucketInfo;

import java.io.IOException;
import java.util.List;

/**
 * <p>
 * <strong>
 * 用一句话描述功能
 * </strong><br /><br />
 * 如题
 * </p>
 *
 * @author chengchao - 2020/4/14 14:11 <br />
 * @see [相关类方法]
 * @since [产品模块版本]
 */
public interface ChossClient {

    void createBucket(String bucketName, String bucketDetail) throws IOException;

    void deleteBucket(String bucketName);

    List<BucketInfo> listBuckets();

    void pubObject(PutRequest putRequest);

    void pubObject(String bucketName, String key, byte[] content, String mediaType);

    void deleteObject(String bucketName, String key);

    ChossObjectSummary getObjectSummary(String bucketName, String key);




}
