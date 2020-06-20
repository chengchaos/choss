package luxe.chaos.choss.auth.dao;

import luxe.chaos.choss.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface UserMapper {


    User getById(String userId);

    User getByAccount(String account);

    long create(User user);

    @Insert("INSERT into users (account, name, password) values (#{user.account}, #{user.name}, #{user.password}) ")
    void addUser(@Param("user") User user);


    void updatePassword(@Param("id") String id,
                        @Param("pwd") String password);

    @Update("UPDATE users SET del_sign = 1 where id = #{id} ")
    void delete(@Param("id") String id);

    long searchResultCount(String q);

    List<User> searchResult(Map<String, Object> queryMap);

}
