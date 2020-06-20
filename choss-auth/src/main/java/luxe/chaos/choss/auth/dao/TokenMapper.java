package luxe.chaos.choss.auth.dao;

import luxe.chaos.choss.entity.Token;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Mapper
@Repository
public interface TokenMapper {


    void add(@Param("token") Token token);

    void delete(@Param("id") String id);

    void update(@Param("id") String id,
                @Param("expireTime") int expireTime,
                @Param("active") int active,
                @Param("refreshTime") Date refreshTime);




    Token getById(@Param("id") String id);

    Token getDefault(String userId);

    List<Token> getByUserId(@Param("userId") String userId);

}
