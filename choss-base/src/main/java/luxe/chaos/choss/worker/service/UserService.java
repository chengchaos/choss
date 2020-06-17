package luxe.chaos.choss.worker.service;

import luxe.chaos.choss.worker.beans.PaginationDataInput;
import luxe.chaos.choss.worker.beans.PaginationDataOutput;
import luxe.chaos.choss.worker.entity.User;

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

    PaginationDataOutput<User> search(String q,  PaginationDataInput pdi) ;

}
