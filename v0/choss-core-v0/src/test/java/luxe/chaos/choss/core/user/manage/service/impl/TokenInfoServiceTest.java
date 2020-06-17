package luxe.chaos.choss.core.user.manage.service.impl;

import luxe.chaos.choss.core.user.manage.model.TokenInfo;
import luxe.chaos.choss.core.user.manage.service.TokenInfoService;
import luxe.chaos.choss.rdbs.mybatis.ChossDataSourceConfig;
import org.junit.Assert;
import org.junit.Ignore;
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
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * <p>
 * <strong>
 * 用一句话描述功能
 * </strong><br /><br />
 * 如题
 * </p>
 *
 * @author chengchao - 2020/4/12 21:22 <br />
 * @see [相关类方法]
 * @since [产品模块版本]
 */
@RunWith(SpringJUnit4ClassRunner.class)
@Import(ChossDataSourceConfig.class)
@PropertySource("classpath:application.properties")
@MapperScan("luxe.chaos.choss.core.user.manage.dao")
@ComponentScan("luxe.chaos.choss.**")
public class TokenInfoServiceTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(TokenInfoServiceTest.class);

    @Autowired
    private TokenInfoService tokenInfoService;

    @Test
    public void addTokenTest() {

        String userId = "idis1";
        TokenInfo tokenInfo = TokenInfo.newTokenInfo(userId);

        this.tokenInfoService.addToken(tokenInfo);
        Assert.assertTrue("Test success", true);

    }

    @Test
    @Ignore
    public void deleteTokenTest() {

        String tokenId = "4ba8f7307190482f90aedc30a1fbb6ad";
        this.tokenInfoService.deleteToken(tokenId);
        Assert.assertTrue("Test success", true);

    }

    @Test
    public void updateToken() {

        String tokenId = "08760a9a90da4e9685c6c8f4fb4e253e";
        TokenInfo tokenInfo = this.tokenInfoService.getTokenInfoById(tokenId);

        if (tokenInfo != null) {
            boolean active = tokenInfo.getActive();
            tokenInfo.setActive(!active);
            int expireTime = tokenInfo.getExpireTime();
            tokenInfo.setExpireTime(expireTime + 1);
            this.tokenInfoService.updateToken(tokenInfo);
        } else {
            LOGGER.warn("data is not exists !!!");
        }


    }


    @Test
    public void getTokenInfosByUserIdTest() {

        String userId = "idis1";
        List<TokenInfo> results = this.tokenInfoService.getTokenInfosByUserId(userId);
        Assert.assertTrue("the list is not empty!!", !CollectionUtils.isEmpty(results));
        results.forEach(bean -> LOGGER.info("get result -=> {}", bean));

    }
}
