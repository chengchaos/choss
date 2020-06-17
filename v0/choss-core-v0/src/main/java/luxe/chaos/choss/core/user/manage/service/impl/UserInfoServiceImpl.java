package luxe.chaos.choss.core.user.manage.service.impl;

import luxe.chaos.choss.core.user.manage.dao.UserInfoMapper;
import luxe.chaos.choss.core.user.manage.model.UserInfo;
import luxe.chaos.choss.core.user.manage.service.UserInfoService;
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
 * @author chengchao - 2020/4/12 12:42 <br />
 * @see [相关类方法]
 * @since [产品模块版本]
 */
@Service
@Transactional
public class UserInfoServiceImpl implements UserInfoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserInfoServiceImpl.class);

    @Autowired
    private UserInfoMapper userInfoMapper;

    public UserInfoServiceImpl(UserInfoMapper userInfoMapper) {
        this.userInfoMapper = userInfoMapper;
    }

    @Override
    public void addUser(UserInfo userInfo) {
        // TODO add token
        LOGGER.info("add user {}", userInfo);
        this.userInfoMapper.addUser(userInfo);
    }

    @Override
    public void deleteUser(String userId) {

        this.userInfoMapper.deleteUser(userId);
        // TODO: 删除 Token and auth ...
    }

    @Override
    public void updateUser(UserInfo userInfo) {
        // TODO 如果 password 为空， 则不修改密码。
        this.userInfoMapper.updateUser(userInfo);
    }

    @Override
    public UserInfo getUserById(String userId) {
        return this.userInfoMapper.getUserById(userId);
    }

    @Override
    public UserInfo getUserByName(String userName) {
        return this.userInfoMapper.getUserByName(userName);
    }

    @Override
    public UserInfo checkPassword(String userName, String password) {
        return this.userInfoMapper.checkPassword(userName, password);
    }
}
