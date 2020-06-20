package luxe.chaos.choss.auth.service;

import luxe.chaos.choss.entity.ServiceAuth;
import org.apache.ibatis.annotations.ResultMap;

public interface ServiceAuthService {

    void add(ServiceAuth serviceAuth);

    void delete(String bucketId, String tokenId);

    void deleteByTokenId(String tokenId);

    void deleteByBucketId( String bucketId);

    @ResultMap("serviceAuthResultMap")
    ServiceAuth getServiceAuth( String bucketId, String tokenId);

    boolean checkToken(String tokenId);
}
