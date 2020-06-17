package luxe.chaos.choss.core.user.manage.dao;

import luxe.chaos.choss.core.user.manage.model.TokenInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;

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
 * @author chengchao - 2020/4/12 18:46 <br />
 * @see [相关类方法]
 * @since [产品模块版本]
 */
public interface TokenInfoMapper {


    void addToken(@Param("tokenInfo") TokenInfo tokenInfo);


    @ResultMap("TokenInfoResultMap")
    TokenInfo getTokenInfoById(@Param("tokenId") String tokenId);

    void deleteToken(@Param("tokenId") String tokenId);

    /**
     * only can update expireTime and isActive ..
     * @param tokenInfo
     */
    void updateToken(@Param("tokenInfo") TokenInfo tokenInfo);

    void refreshToken(@Param("tokenId") String tokenId, @Param("refreshTime") Date refreshTime);


    @ResultMap("TokenInfoResultMap")
    List<TokenInfo> getTokenInfosByUserId(@Param("userId") String userId);

}
