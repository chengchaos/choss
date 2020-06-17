package luxe.chaos.choss.core.user.manage.service.impl;

import luxe.chaos.choss.core.ChossCoreApp;
import luxe.chaos.choss.core.user.manage.model.SystemRole;
import luxe.chaos.choss.core.user.manage.model.UserInfo;
import luxe.chaos.choss.core.user.manage.service.UserInfoService;
import luxe.chaos.choss.core.utils.CoreUtil;
import luxe.chaos.choss.rdbs.mybatis.ChossDataSourceConfig;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;

/**
 * <p>
 * <strong>
 * 用一句话描述功能
 * </strong><br /><br />
 * 如题
 * </p>
 *
 * @author chengchao - 2020/4/12 13:19 <br />
 * @see [相关类方法]
 * @since [产品模块版本]
 */
@RunWith(SpringJUnit4ClassRunner.class)
@Import(ChossDataSourceConfig.class)
@PropertySource("classpath:application.properties")
@MapperScan("luxe.chaos.choss.core.user.manage.dao")
@ComponentScan("luxe.chaos.choss.**")
//@SpringBootTest(classes = ChossCoreApp.class)
public class UserInfoServiceTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserInfoServiceTest.class);

    /**
     * @Autowired 的注入规则，是在同类型的接口有多个实现时优先按属性名注入，
     * 只有一个实现时按类型注入。看来原因是 Mybatis 把 basePackages 下所有接口都注册成 MapperProxy 了。
     */
    @Autowired
//    @Qualifier("userInfoServiceImpl")
    private UserInfoService userInfoService;


    @Test
    public void addUserTest() {

        UserInfo userInfo = new UserInfo();

        userInfo.setId("idis6");
        userInfo.setCreateTime(new Date());
        userInfo.setSystemRole(SystemRole.USER);
        userInfo.setUserName("fenghaodong_006");
        userInfo.setUserPwd("over2012");
        userInfo.setUserDetail("扫地只是我的表面工作");

        System.out.println(userInfo);
        System.out.println(this.userInfoService);
        System.out.println("--------");

        this.userInfoService.addUser(userInfo);

        LOGGER.info("userInfo -=> {}", userInfo);

        Assert.assertTrue("Test success.", true);
    }

    @Test
    public void deleteUserTest() {
        String userId = "idis5";
        this.userInfoService.deleteUser(userId);
        Assert.assertTrue("Test success.", true);
    }


    @Test
    public void updateUserTest() {

        String userId = "idis1";
        UserInfo userInfo = this.userInfoService.getUserById(userId);

        if (userInfo != null) {
            LOGGER.info("selected -------------------- {}", userInfo);
            userInfo.setUserPwd(String.valueOf(System.currentTimeMillis()));
            userInfo.setUserDetail("呵呵呵呵呵");
            this.userInfoService.updateUser(userInfo);

        } else {
            LOGGER.info("Data isn't exists .");
        }

    }

    @Test
    public void getUserByIdTest() {

        String userId = "idis1";
        UserInfo userInfo = this.userInfoService.getUserById(userId);
        Assert.assertNotNull("UserInfo not null", userInfo);
        LOGGER.info("userInfo -=> {}", userInfo);
    }

    @Test
    public void getUserByNameTest() {

        String userNeme = "chengchao";
        UserInfo userInfo = this.userInfoService.getUserByName(userNeme);
        Assert.assertNotNull("UserInfo not null", userInfo);
        LOGGER.info("userInfo -=> {}", userInfo);
    }

    @Test
    public void checkPasswordTest() {
        String userName = "handong";
        String userPwd = "over2012";

        UserInfo userInfo = this.userInfoService.checkPassword(userName, userPwd);

        Assert.assertNotNull("UserInfo not null", userInfo);
        LOGGER.info("userInfo -=> {}", userInfo);
    }
}
