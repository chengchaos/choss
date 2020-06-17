package luxe.chaos.choss.core.user.manage.service;

import luxe.chaos.choss.core.user.manage.model.TokenInfo;

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
 * @author chengchao - 2020/4/12 18:48 <br />
 * @see [相关类方法]
 * @since [产品模块版本]
 */
public interface TokenInfoService {

    void addToken(TokenInfo tokenInfo);

    void deleteToken(String tokenId);

    /**
     * only can update expireTime and isActive ..
     * @param tokenInfo
     */
    void updateToken(TokenInfo tokenInfo);

    void refreshToken(String tokenId, Date refrestTime);

    TokenInfo getTokenInfoById(String tokenId);

    List<TokenInfo> getTokenInfosByUserId(String userId);


}
