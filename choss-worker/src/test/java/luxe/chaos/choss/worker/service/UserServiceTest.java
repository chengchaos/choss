package luxe.chaos.choss.worker.service;

import io.netty.buffer.ByteBufUtil;
import luxe.chaos.choss.worker.ChossWorkerApplication;
import luxe.chaos.choss.worker.beans.PaginationDataInput;
import luxe.chaos.choss.worker.beans.PaginationDataOutput;
import luxe.chaos.choss.worker.entity.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;


@SpringBootTest(classes = ChossWorkerApplication.class,
webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class UserServiceTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceTest.class);

    @Autowired
    private UserService userService;

    @Test
    public void diTest() {
        LOGGER.info("userService => {}", userService);
        Assertions.assertNotNull(userService, "DI Failure.");
    }

    @Test
    public void getByAccountTest() {

        Optional<User> userOptional = this.userService.getByAccount("chengchaos@126.com");

        Assertions.assertTrue(userOptional.isPresent());

        User user = userOptional.get();
        LOGGER.info("user => {}", user);

    }


    @Test
    public void createTest() throws NoSuchAlgorithmException {

        User user = new User();
        user.setAccount("chao.cheng 3@futuremove.cn");
        user.setName("cheng chao");
        MessageDigest md5 = MessageDigest.getInstance("MD5");

        byte[] bytes = md5.digest("admin".getBytes(StandardCharsets.UTF_8));
        String s = ByteBufUtil.hexDump(bytes);


        user.setPassword(s);

        LOGGER.info("user => {}", user);
        this.userService.create(user);

    }


    @Test
    public void deleteTest() throws NoSuchAlgorithmException {
        this.userService.delete("1");
    }

    @Test
    public void searchTest() {

        String q = "a";
        PaginationDataInput pdi = new PaginationDataInput();
        pdi.setPageNum(1);
        pdi.setPageSize(1);

        PaginationDataOutput<User> result = this.userService.search(q, pdi);

        Assertions.assertTrue(result.getData().size() > 0);

        LOGGER.info(" result => {}", result);
    }
}
