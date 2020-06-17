package luxe.chaos.choss.worker.service.impl;

import luxe.chaos.choss.worker.dao.ServiceAuthMapper;
import luxe.chaos.choss.worker.dao.TokenMapper;
import luxe.chaos.choss.worker.entity.Token;
import luxe.chaos.choss.worker.service.TokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class TokenServiceImpl implements TokenService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TokenServiceImpl.class);

    private TokenMapper tokenMapper;


    private ServiceAuthMapper serviceAuthMapper;

    @Autowired
    public void setTokenMapper(TokenMapper tokenMapper) {
        this.tokenMapper = tokenMapper;
    }

    @Autowired
    public void setServiceAuthMapper(ServiceAuthMapper serviceAuthMapper) {
        this.serviceAuthMapper = serviceAuthMapper;
    }

    @Override
    public void add(Token token) {
        this.tokenMapper.add(token);
    }

    @Override
    public void delete(String id) {
        this.serviceAuthMapper.deleteByTokenId(id);
        this.tokenMapper.delete(id);
    }

    @Override
    public void update(String id, int expireTime, int active, Date refreshTime) {
        this.tokenMapper.update(id, expireTime, active,refreshTime);
    }



    @Override
    public Token getById(String id) {
        return tokenMapper.getById(id);
    }

    @Override
    public List<Token> getByUserId(String userId) {
        return this.tokenMapper.getByUserId(userId);
    }
}
