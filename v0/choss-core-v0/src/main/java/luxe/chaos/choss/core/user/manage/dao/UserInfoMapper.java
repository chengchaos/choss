package luxe.chaos.choss.core.user.manage.dao;

import luxe.chaos.choss.core.user.manage.model.UserInfo;
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
 * @author chengchao - 2020/4/12 12:18 <br />
 * @see [相关类方法]
 * @since [产品模块版本]
 */
public interface UserInfoMapper {

    void addUser(@Param("userInfo") UserInfo userInfo);

    void deleteUser(@Param("userId") String userId);

    void updateUser(@Param("userInfo") UserInfo userInfo);

    @ResultMap("UserInfoResultMap")
    UserInfo getUserById(@Param("userId") String userId);

    @ResultMap("UserInfoResultMap")
    UserInfo getUserByName(@Param("userName") String userName);

    UserInfo checkPassword(@Param("userName") String userName,
                           @Param("userPwd") String userPwd);
}
