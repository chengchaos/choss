package luxe.chaos.choss.worker.dao;

import luxe.chaos.choss.worker.entity.Bucket;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface BucketMapper {

    void add(@Param("bucket") Bucket bucket);

    void delete(@Param("id") String id);

    void update(@Param("bucket") Bucket bucket);

    Bucket getById(@Param("id") String id);

    List<Bucket> getByUserId(@Param("userId") String userId);

    List<Bucket> getByTokenId(@Param("tokenId") String tokenId);

}
