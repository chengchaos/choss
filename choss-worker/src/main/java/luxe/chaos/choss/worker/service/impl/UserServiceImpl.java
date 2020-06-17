package luxe.chaos.choss.worker.service.impl;

import luxe.chaos.choss.util.CodingHelper;
import luxe.chaos.choss.util.DateHelper;
import luxe.chaos.choss.worker.beans.PaginationDataInput;
import luxe.chaos.choss.worker.beans.PaginationDataOutput;
import luxe.chaos.choss.worker.dao.ServiceAuthMapper;
import luxe.chaos.choss.worker.dao.TokenMapper;
import luxe.chaos.choss.worker.dao.UserMapper;
import luxe.chaos.choss.worker.entity.Token;
import luxe.chaos.choss.worker.entity.User;
import luxe.chaos.choss.worker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class UserServiceImpl implements UserService {


    private UserMapper userMapper;
    private TokenMapper tokenMapper;
    private ServiceAuthMapper serviceAuthMapper;

    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }
    @Autowired
    public void setTokenMapper(TokenMapper tokenMapper) {
        this.tokenMapper = tokenMapper;
    }

    @Autowired
    public void setServiceAuthMapper(ServiceAuthMapper serviceAuthMapper) {
        this.serviceAuthMapper = serviceAuthMapper;
    }

    @Override
    public void create(User user) {
        // 创建用户的同时也创建用户自己的 token
        // 用户的 token 永不过期
        String id = CodingHelper.newUuid();
        String pwd = CodingHelper.md5hex(user.getPassword());

        user.setId(id);
        user.setPassword(pwd);

        Token token = Token.newInstance(id, new Date(DateHelper.YEAR_9999));
        tokenMapper.add(token);

//        this.userMapper.addUser(user);
        this.userMapper.create(user);

    }

    @Override
    public void updatePassword(String id, String password) {
        String pwd = CodingHelper.md5hex(password);
        this.userMapper.updatePassword(id, pwd);
    }

    @Override
    public void delete(String id) {

        List<Token> tokenList = this.tokenMapper.getByUserId(id);
        if (tokenList != null && !tokenList.isEmpty()) {
            tokenList.stream()
                    .map(Token::getId)
                    .forEach(tokenId -> {
                        this.serviceAuthMapper.deleteByTokenId(tokenId);
                        this.tokenMapper.delete(tokenId);
                    });
        }

        this.userMapper.delete(id);
    }

    @Override
    public Optional<User> getByAccount(String account) {
        User user = this.userMapper.getByAccount(account);
        return Optional.ofNullable(user);
    }

    @Override
    public PaginationDataOutput<User> search(String q, PaginationDataInput pdi) {

        String query = '%' + q + '%';
        q = null;

        int pageNum = pdi.getPageNum();
        if (pageNum <= 0) {
            pageNum = 1;
        }

        int limit = pdi.getPageSize();

        if (limit <= 0) {
            limit = 10;
        }

        long offset = (pageNum - 1) * limit;


        long count = this.userMapper.searchResultCount(query);

        Map<String, Object> queryMap = new HashMap<>();
        queryMap.put("query", query);
        queryMap.put("limit", limit);
        queryMap.put("offset", offset);

        List<User> userList = this.userMapper.searchResult(queryMap);
        long maxPage = 1L;
        if (count > 0) {
            long max = count / limit;
            if (count % limit != 0) {
                maxPage = max + 1;
            } else {
                maxPage = max;
            }
        }

        PaginationDataOutput<User> result = new PaginationDataOutput<>();
        result.setCurrentPage(pageNum);
        result.setMaxPage(maxPage);
        result.setPageSize(limit);
        result.setTotality(count);
        result.setData(userList);

        return result;
    }
}
