package luxe.chaos.choss.core.user.manage.service.impl;

import luxe.chaos.choss.core.user.manage.dao.TokenInfoMapper;
import luxe.chaos.choss.core.user.manage.model.TokenInfo;
import luxe.chaos.choss.core.user.manage.service.TokenInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * <strong>
 * 用一句话描述功能
 * </strong><br /><br />
 * 如题
 * </p>
 *
 * @author chengchao - 2020/4/12 21:19 <br />
 * @see [相关类方法]
 * @since [产品模块版本]
 */
@Service
@Transactional
public class TokenInfoServiceImpl implements TokenInfoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TokenInfoServiceImpl.class);

    @Autowired
    private TokenInfoMapper tokenInfoMapper;

    @Override
    public void addToken(TokenInfo tokenInfo) {

        this.tokenInfoMapper.addToken(tokenInfo);

    }

    @Override
    public void deleteToken(String tokenId) {
        this.tokenInfoMapper.deleteToken(tokenId);
    }

    @Override
    public void updateToken(TokenInfo tokenInfo) {
        this.tokenInfoMapper.updateToken(tokenInfo);
    }

    @Override
    public void refreshToken(String tokenId, Date refrestTime) {
        this.tokenInfoMapper.refreshToken(tokenId, refrestTime);
    }

    @Override
    public TokenInfo getTokenInfoById(String tokenId) {
        return this.tokenInfoMapper.getTokenInfoById(tokenId);
    }

    @Override
    public List<TokenInfo> getTokenInfosByUserId(String userId) {
        return this.tokenInfoMapper.getTokenInfosByUserId(userId);
    }
}
