package luxe.chaos.choss.core.user.manage.service;

import luxe.chaos.choss.core.user.manage.model.BucketInfo;
import org.apache.ibatis.annotations.ResultMap;

import java.util.List;

/**
 * <p>
 * <strong>
 * 用一句话描述功能
 * </strong><br /><br />
 * 如题
 * </p>
 *
 * @author chengchao - 2020/4/12 20:04 <br />
 * @see [相关类方法]
 * @since [产品模块版本]
 */
public interface BucketInfoService {


    void addBucket(BucketInfo bucketInfo);

    void deleteBucket(String bucketId);

    void updateBucket(String bucketName, String bucketDetail);

    @ResultMap("BucketInfoResultMap")
    BucketInfo getBucketById(String bucketId);

    @ResultMap("BucketInfoResultMap")
    BucketInfo getBucketByName(String userId, String bucketName);

    @ResultMap("BucketInfoResultMap")
    List<BucketInfo> getBucketsByUserId(String userId);

    @ResultMap("BucketInfoResultMap")
    List<BucketInfo> getBucketsByToken(String  tokenId);




}
