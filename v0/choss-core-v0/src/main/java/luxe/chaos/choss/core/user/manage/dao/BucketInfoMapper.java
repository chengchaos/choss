package luxe.chaos.choss.core.user.manage.dao;

import luxe.chaos.choss.core.user.manage.model.BucketInfo;
import org.apache.ibatis.annotations.Param;
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
 * @author chengchao - 2020/4/12 20:12 <br />
 * @see [相关类方法]
 * @since [产品模块版本]
 */
public interface BucketInfoMapper {

    void addBucket(@Param("bucketInfo") BucketInfo bucketInfo);

    void deleteBucket(@Param("bucketId") String bucketId);

    void updateBucket(@Param("bucketName") String bucketName, @Param("bucketDetail") String bucketDetail);

    @ResultMap("BucketInfoResultMap")
    BucketInfo getBucketById(@Param("bucketId") String bucketId);

//    @ResultMap("BucketInfoResultMap")
//    BucketInfo getBucketByName(@Param("userId") String userId, @Param("bucketName") String bucketName);

    @ResultMap("BucketInfoResultMap")
    List<BucketInfo> getBucketsByUserId(@Param("userId") String userId);

    @ResultMap("BucketInfoResultMap")
    List<BucketInfo> getBucketsByToken(@Param("tokenId") String tokenId);


}
