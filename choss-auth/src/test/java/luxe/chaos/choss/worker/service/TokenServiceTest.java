package luxe.chaos.choss.worker.service;


import luxe.chaos.choss.auth.service.TokenService;
import luxe.chaos.choss.util.CodingHelper;
import luxe.chaos.choss.auth.ChossWorkerApplication;
import luxe.chaos.choss.entity.Token;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;

@SpringBootTest(classes = ChossWorkerApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class TokenServiceTest {


    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceTest.class);

    @Autowired
    private TokenService tokenService;

    @Test
    public void addTest() {

        Token token = new Token();
        Date date = new Date();
        token.setRefreshTime(date);
        token.setCreateTime(date);

        String id = CodingHelper.newUuid();
        token.setUserId(id);
        token.setId(id);

        this.tokenService.add(token);
    }

    @Test
    public void deleteTest() {

        String tokenId = "f7442c9ac9de4a9ca79ded83e14c80a7";
        this.tokenService.delete(tokenId);

    }

    @Test
    public void updateTest() {

        String tokenId = "e3efb8a32cdd454d921f04b498c0243e";
        this.tokenService.update(tokenId, 999, 0, new Date());

    }

    @Test
    public void getByIdTest() {
        String tokenId = "e3efb8a32cdd454d921f04b498c0243e";
        Token token = this.tokenService.getById(tokenId);
        LOGGER.info("token => {}", token);
    }


    @Test
    public void getByUserIdTest() {
        String userId = "e3efb8a32cdd454d921f04b498c0243e";
        List<Token> tokens = this.tokenService.getByUserId(userId);
        LOGGER.info("token => {}", tokens);
    }
}
