package luxe.chaos.choss.worker.service;

import luxe.chaos.choss.worker.entity.Token;

import java.util.Date;
import java.util.List;

public interface TokenService {


    void add( Token token);

    void delete( String id);

    void update(String id, int expireTime, int active, Date refreshTime);

    Token getById(String id);

    List<Token> getByUserId( String userId);
}
