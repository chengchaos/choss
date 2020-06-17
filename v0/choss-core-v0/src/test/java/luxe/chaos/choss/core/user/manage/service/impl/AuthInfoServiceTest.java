package luxe.chaos.choss.core.user.manage.service.impl;

import luxe.chaos.choss.core.user.manage.model.AuthInfo;
import luxe.chaos.choss.core.user.manage.service.AuthInfoService;
import luxe.chaos.choss.rdbs.mybatis.ChossDataSourceConfig;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.Objects;

/**
 * <p>
 * <strong>
 * 用一句话描述功能
 * </strong><br /><br />
 * 如题
 * </p>
 *
 * @author chengchao - 2020/4/12 22:52 <br />
 * @see [相关类方法]
 * @since [产品模块版本]
 */
@RunWith(SpringJUnit4ClassRunner.class)
@Import(ChossDataSourceConfig.class)
@PropertySource("classpath:application.properties")
@MapperScan("luxe.chaos.choss.core.user.manage.dao")
@ComponentScan("luxe.chaos.choss.**")
public class AuthInfoServiceTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthInfoServiceTest.class);

    @Autowired
    private AuthInfoService authInfoService;

    @Test
    public void addAuthTest() {

        String bucketId = "idis2#web";
        String targetToken = "08760a9a90da4e9685c6c8f4fb4e253e";

        AuthInfo authInfo = new AuthInfo();
        authInfo.setBucketId(bucketId);
        authInfo.setTargetToken(targetToken);
        authInfo.setCreateTime(new Date());

        this.authInfoService.addAuth(authInfo);

        Assert.assertTrue("Add AuthInfo not successful !!!", true );

    }


    @Test
    public void getAuthTest() {

        String bucketId = "idis2#web";
        String targetToken = "08760a9a90da4e9685c6c8f4fb4e253e";

        AuthInfo bean = this.authInfoService.getAuth(bucketId, targetToken);

        Assert.assertNotNull("Get AuthInfo not successful !", bean);

        LOGGER.info("AuthInfo -=> {}", bean);

    }


}
