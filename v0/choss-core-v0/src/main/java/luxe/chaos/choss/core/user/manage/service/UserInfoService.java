package luxe.chaos.choss.core.user.manage.service;

import luxe.chaos.choss.core.user.manage.model.UserInfo;

/**
 * <p>
 * <strong>
 * 用一句话描述功能
 * </strong><br /><br />
 * 如题
 * </p>
 *
 * @author chengchao - 2020/4/12 12:39 <br />
 * @see [相关类方法]
 * @since [产品模块版本]
 */
public interface UserInfoService {


    void addUser(UserInfo userInfo);

    void deleteUser(String userId);

    void updateUser(UserInfo userInfo);

    UserInfo getUserById(String userId);

    UserInfo getUserByName(String userName);

    UserInfo checkPassword(String userName, String password);
}
