package luxe.chaos.choss;

import luxe.chaos.choss.registry.RegistryApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = RegistryApplication.class,
webEnvironment = SpringBootTest.WebEnvironment.NONE)
class RegistryApplicationTests {

	@Test
	void contextLoads() {
	}

}
