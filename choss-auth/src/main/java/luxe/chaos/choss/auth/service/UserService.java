package luxe.chaos.choss.auth.service;

import luxe.chaos.choss.entity.User;
import luxe.chaos.choss.util.PaginationDataInput;
import luxe.chaos.choss.util.PaginationDataOutput;

import java.util.Optional;

public interface UserService {


    /**
     * 创建用户的同时也创建用户自己的 token
     * @param user
     * @return
     */
    void add(User user);

    void updatePassword(String id, String password);

    /**
     * 删除用户的同时也删除用户的 token 以及和 token 关联的权限。
     * @param id
     */
    void delete(String id);


    Optional<User> getByAccount(String account);

    PaginationDataOutput<User> search(String q, PaginationDataInput pdi) ;

}
