package luxe.chaos.choss.core.user.manage.service.impl;

import luxe.chaos.choss.core.user.manage.model.BucketInfo;
import luxe.chaos.choss.core.user.manage.service.BucketInfoService;
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
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;

/**
 * <p>
 * <strong>
 * 用一句话描述功能
 * </strong><br /><br />
 * 如题
 * </p>
 *
 * @author chengchao - 2020/4/12 22:08 <br />
 * @see [相关类方法]
 * @since [产品模块版本]
 */
@RunWith(SpringJUnit4ClassRunner.class)
@Import(ChossDataSourceConfig.class)
@PropertySource("classpath:application.properties")
@MapperScan("luxe.chaos.choss.core.user.manage.dao")
@ComponentScan("luxe.chaos.choss.**")
public class BucketInfoServiceTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(BucketInfoServiceTest.class);

    @Autowired
    private BucketInfoService bucketInfoService;


    @Test
    public void addBucketTest() {
        String userId = "idis2";
        String bucketName = "web";

        BucketInfo bucketInfo = BucketInfo.newBucketInfo(userId, bucketName);
        this.bucketInfoService.addBucket(bucketInfo);

        Assert.assertTrue("Test NOT successful !!!", true );
    }

    @Test(expected = org.springframework.dao.DuplicateKeyException.class)
    public void addBucketTestException() {
        String userId = "idis1";
        String bucketName = "web";

        BucketInfo bucketInfo = BucketInfo.newBucketInfo(userId, bucketName);
        this.bucketInfoService.addBucket(bucketInfo);

        Assert.assertTrue("Test NOT successful !!!", true );
    }


    @Test
    public void deleteBucketTest() {

        String bucketId = "idis1#web";
        this.bucketInfoService.deleteBucket(bucketId);

        Assert.assertTrue("Test NOT successful !!!", true );
    }


    @Test
    public void getBucketByIdTest() {
        String bucketId = "idis2#web";
        BucketInfo bean = this.bucketInfoService.getBucketById(bucketId);
        Assert.assertNotNull("Test NOT successful !!! ", Objects.nonNull(bean));
        LOGGER.info("bucketInfo -=> {}", bean);
    }

    @Test
    public void getBucketsByUserIdTest() {

        String userId = "idis2";
        List<BucketInfo> results = this.bucketInfoService.getBucketsByUserId(userId);
        Assert.assertTrue("List is null or empty !!!", !CollectionUtils.isEmpty(results));
        results.forEach(bean -> LOGGER.info(" bucket info is ... {}", bean));
    }
}
