package luxe.chaos.choss.worker.service;

import luxe.chaos.choss.auth.ChossWorkerApplication;
import luxe.chaos.choss.entity.ServiceAuth;
import luxe.chaos.choss.auth.service.ServiceAuthService;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

@SpringBootTest(classes = ChossWorkerApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class ServiceAuthServiceTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceAuthServiceTest.class);

    @Autowired
    private ServiceAuthService serviceAuthService;

    @Test
    public void addTest() {

        ServiceAuth auth = new ServiceAuth();
        auth.setBucketId("e3efb8a32cdd454d921f04b498c0243e");
        auth.setTokenId("e3efb8a32cdd454d921f04b498c0243e");
        auth.setCreateTime(new Date());
        serviceAuthService.add(auth);
    }


    @Test
    public void deleteTest() {

        serviceAuthService.delete("e3efb8a32cdd454d921f04b498c0243e", "e3efb8a32cdd454d921f04b498c0243e");
    }

    @Test
    public void deleteByTokenIdTest() {

        serviceAuthService.deleteByTokenId("e3efb8a32cdd454d921f04b498c0243e");
    }

    @Test
    public void deleteByBucketIdTest() {

        serviceAuthService.deleteByBucketId("e3efb8a32cdd454d921f04b498c0243e");
    }

    @Test
    public void getServiceAuthTest() {

        ServiceAuth serviceAuth = serviceAuthService.getServiceAuth("e3efb8a32cdd454d921f04b498c0243e",
                "e3efb8a32cdd454d921f04b498c0243e");

        LOGGER.info("serviceAuth => {}", serviceAuth.getCreateTime());
    }
}
