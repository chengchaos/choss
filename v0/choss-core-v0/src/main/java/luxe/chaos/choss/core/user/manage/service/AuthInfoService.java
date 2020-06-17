package luxe.chaos.choss.core.user.manage.service;

import luxe.chaos.choss.core.user.manage.model.AuthInfo;

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
public interface AuthInfoService {

    void addAuth(AuthInfo authInfo);

    AuthInfo getAuth(String bucketId, String targetToken);

    void deleteAuth(String bucketId, String targetToken);

    void deleteAuthByToken(String targetToken);

    void deleteAuthByBucket(String bucketId);


}
