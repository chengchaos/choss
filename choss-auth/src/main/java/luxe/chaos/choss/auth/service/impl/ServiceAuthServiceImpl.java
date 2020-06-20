package luxe.chaos.choss.auth.service.impl;

import luxe.chaos.choss.auth.dao.ServiceAuthMapper;
import luxe.chaos.choss.auth.dao.TokenMapper;
import luxe.chaos.choss.entity.ServiceAuth;
import luxe.chaos.choss.entity.Token;
import luxe.chaos.choss.auth.service.ServiceAuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;

@Service
@Transactional
public class ServiceAuthServiceImpl implements ServiceAuthService {


    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceAuthServiceImpl.class);

    private ServiceAuthMapper serviceAuthMapper;
    private TokenMapper tokenMapper;

    @Autowired
    public void setServiceAuthMapper(ServiceAuthMapper serviceAuthMapper) {
        this.serviceAuthMapper = serviceAuthMapper;
    }
    @Autowired
    public void setTokenMapper(TokenMapper tokenMapper) {
        this.tokenMapper = tokenMapper;
    }

    @Override
    public void add(ServiceAuth serviceAuth) {
        this.serviceAuthMapper.add(serviceAuth);

    }

    @Override
    public void delete(String bucketId, String tokenId) {
        this.serviceAuthMapper.delete(bucketId, tokenId);
    }

    @Override
    public void deleteByTokenId(String tokenId) {
        this.serviceAuthMapper.deleteByTokenId(tokenId);
    }

    @Override
    public void deleteByBucketId(String bucketId) {
        this.serviceAuthMapper.deleteByBucketId(bucketId);
    }

    @Override
    public ServiceAuth getServiceAuth(String bucketId, String tokenId) {
        return this.serviceAuthMapper.getServiceAuth(bucketId, tokenId);
    }


    @Override
    public boolean checkToken(String tokenId) {
        Token token = this.tokenMapper.getById(tokenId);
        if (token == null ) {
            return false;
        }

        if (token.getActive() == 0) {
            return false;
        }

        Date current = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(token.getRefreshTime());
        calendar.add(Calendar.DATE, token.getExpireTime());

        return current.before(calendar.getTime());
    }
}
