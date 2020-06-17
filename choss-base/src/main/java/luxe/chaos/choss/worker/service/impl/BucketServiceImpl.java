package luxe.chaos.choss.worker.service.impl;

import luxe.chaos.choss.worker.dao.BucketMapper;
import luxe.chaos.choss.worker.dao.ServiceAuthMapper;
import luxe.chaos.choss.worker.dao.TokenMapper;
import luxe.chaos.choss.worker.dao.UserMapper;
import luxe.chaos.choss.worker.entity.Bucket;
import luxe.chaos.choss.worker.entity.ServiceAuth;
import luxe.chaos.choss.worker.entity.Token;
import luxe.chaos.choss.worker.entity.User;
import luxe.chaos.choss.worker.service.BucketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class BucketServiceImpl implements BucketService {


    private static final Logger LOGGER = LoggerFactory.getLogger(BucketServiceImpl.class);

    private ServiceAuthMapper serviceAuthMapper;
    private TokenMapper tokenMapper;
    private BucketMapper bucketMapper;
    private UserMapper userMapper;

    @Autowired
    public void setServiceAuthMapper(ServiceAuthMapper serviceAuthMapper) {
        this.serviceAuthMapper = serviceAuthMapper;
    }

    @Autowired
    public void setTokenMapper(TokenMapper tokenMapper) {
        this.tokenMapper = tokenMapper;
    }

    @Autowired
    public void setBucketMapper(BucketMapper bucketMapper) {
        this.bucketMapper = bucketMapper;
    }

    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public void add(Bucket bucket) {

        String userId = bucket.getUserId();
        // 同时添加用户自己的访问权限
        User user = this.userMapper.getById(userId);
        if (user == null) {
            throw new IllegalStateException("user is NOT exist !!!");
        }

        Token token = this.tokenMapper.getDefault(userId);
        if (token == null) {
            throw new IllegalStateException("token is NOT exist !!!");
        }


        ServiceAuth auth = new ServiceAuth();
        auth.setCreateTime(new Date());
        auth.setTokenId(token.getId());
        auth.setBucketId(bucket.getId());

        this.serviceAuthMapper.add(auth);

        this.bucketMapper.add(bucket);

    }

    @Override
    public void delete(String id) {
        this.serviceAuthMapper.deleteByBucketId(id);
        this.bucketMapper.delete(id);
    }

    @Override
    public void update(Bucket bucket) {
        this.bucketMapper.update(bucket);

    }

    @Override
    public Bucket getById(String id) {
        return this.bucketMapper.getById(id);
    }

    @Override
    public List<Bucket> getByUserId(String userId) {
        return this.bucketMapper.getByUserId(userId);
    }

    @Override
    public List<Bucket> getByTokenId(String tokenId) {
        return this.bucketMapper.getByTokenId(tokenId);
    }
}
