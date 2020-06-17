package luxe.chaos.choss.core.user.manage.service.impl;

import luxe.chaos.choss.core.user.manage.dao.BucketInfoMapper;
import luxe.chaos.choss.core.user.manage.model.BucketInfo;
import luxe.chaos.choss.core.user.manage.service.BucketInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * <strong>
 * 用一句话描述功能
 * </strong><br /><br />
 * 如题
 * </p>
 *
 * @author chengchao - 2020/4/12 22:07 <br />
 * @see [相关类方法]
 * @since [产品模块版本]
 */
@Service
@Transactional
public class BucketInfoServiceImpl implements BucketInfoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BucketInfoServiceImpl.class);

    @Autowired
    private BucketInfoMapper bucketInfoMapper;

    @Override
    public void addBucket(BucketInfo bucketInfo) {
        this.bucketInfoMapper.addBucket(bucketInfo);
    }

    @Override
    public void deleteBucket(String bucketId) {
        this.bucketInfoMapper.deleteBucket(bucketId);
    }

    @Override
    public void updateBucket(String bucketName, String bucketDetail) {

    }

    @Override
    public BucketInfo getBucketById(String bucketId) {
        return this.bucketInfoMapper.getBucketById(bucketId);
    }

    @Override
    public BucketInfo getBucketByName(String userId, String bucketName) {
        return this.getBucketById(userId + "#" + bucketName);
    }

    @Override
    public List<BucketInfo> getBucketsByUserId(String userId) {
        return this.bucketInfoMapper.getBucketsByUserId(userId);
    }

    @Override
    public List<BucketInfo> getBucketsByToken(String tokenId) {
        throw new UnsupportedOperationException("方法未实现");
    }
}
