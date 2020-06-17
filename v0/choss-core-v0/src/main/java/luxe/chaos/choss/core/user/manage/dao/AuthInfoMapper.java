package luxe.chaos.choss.core.user.manage.dao;

import luxe.chaos.choss.core.user.manage.model.AuthInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;

/**
 * <p>
 * <strong>
 * 用一句话描述功能
 * </strong><br /><br />
 * 如题
 * </p>
 *
 * @author chengchao - 2020/4/12 18:47 <br />
 * @see [相关类方法]
 * @since [产品模块版本]
 */
public interface AuthInfoMapper {

    void addAuth(@Param("authInfo") AuthInfo authInfo);

    @ResultMap("AuthInfoResultMap")
    AuthInfo getAuth(@Param("bucketId") String bucketId, @Param("targetToken") String targetToken);


    void deleteAuth(@Param("bucketId") String bucketId, @Param("targetToken") String targetToken);

    void deleteAuthByToken(@Param("targetToken") String targetToken);

    void deleteAuthByBucket(@Param("bucketId") String bucketId);


}
