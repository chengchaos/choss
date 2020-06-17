package luxe.chaos.choss.worker.dao;

import luxe.chaos.choss.worker.entity.ServiceAuth;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface ServiceAuthMapper {

    void add(@Param("auth") ServiceAuth serviceAuth);

    void delete(@Param("bucketId") String bucketId,
                @Param("tokenId") String tokenId);
    void deleteByTokenId(@Param("tokenId") String tokenId);

    void deleteByBucketId(@Param("bucketId") String bucketId);


    ServiceAuth getServiceAuth(@Param("bucketId") String bucketId,
                               @Param("tokenId") String tokenId);
}
