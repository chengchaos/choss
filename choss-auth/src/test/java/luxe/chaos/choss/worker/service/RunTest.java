package luxe.chaos.choss.worker.service;

import luxe.chaos.choss.auth.service.BucketService;
import luxe.chaos.choss.auth.service.UserService;
import luxe.chaos.choss.util.CodingHelper;
import luxe.chaos.choss.auth.ChossWorkerApplication;
import luxe.chaos.choss.entity.Bucket;
import luxe.chaos.choss.entity.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.Optional;

@SpringBootTest(classes = ChossWorkerApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class RunTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(RunTest.class);

    @Autowired
    private UserService userService;

    @Autowired
    private BucketService bucketService;

    private String account = "chengchaos@gmail.com";
    @Test
    public void createUserTest() {

        User user = new User();
        user.setId(CodingHelper.newUuid());
        user.setAccount(account);
        user.setPassword("admin");
        user.setName("Admin");
        user.setCreateTime(new Date());

        this.userService.add(user);

    }

    @Test
    public void createBucketTest() {

         Optional<User> userOptional = this.userService.getByAccount(account);

         if (!userOptional.isPresent()) {
             Assertions.fail();
         }

         User user = userOptional.get();

        Bucket bucket = Bucket.newInstance(user.getId(), "MyObjects");

        this.bucketService.add(bucket);

    }
}
