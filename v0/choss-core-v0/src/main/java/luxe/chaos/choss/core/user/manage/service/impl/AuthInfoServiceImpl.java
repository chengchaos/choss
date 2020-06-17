package luxe.chaos.choss.core.user.manage.service.impl;

import luxe.chaos.choss.core.user.manage.dao.AuthInfoMapper;
import luxe.chaos.choss.core.user.manage.model.AuthInfo;
import luxe.chaos.choss.core.user.manage.service.AuthInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * <strong>
 * 用一句话描述功能
 * </strong><br /><br />
 * 如题
 * </p>
 *
 * @author chengchao - 2020/4/12 22:53 <br />
 * @see [相关类方法]
 * @since [产品模块版本]
 */
@Service
@Transactional
public class AuthInfoServiceImpl implements AuthInfoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthInfoServiceImpl.class);

    @Autowired
    private AuthInfoMapper authInfoMapper;

    @Override
    public void addAuth(AuthInfo authInfo) {
        this.authInfoMapper.addAuth(authInfo);
    }

    @Override
    public void deleteAuth(String bucketId, String targetToken) {
        this.authInfoMapper.deleteAuth(bucketId, targetToken);
    }

    @Override
    public void deleteAuthByToken(String targetToken) {
        this.authInfoMapper.deleteAuthByToken(targetToken);
    }

    @Override
    public void deleteAuthByBucket(String bucketId) {
        this.authInfoMapper.deleteAuthByBucket(bucketId);
    }

    @Override
    public AuthInfo getAuth(String bucketId, String targetToken) {
        return this.authInfoMapper.getAuth(bucketId, targetToken);
    }
}
