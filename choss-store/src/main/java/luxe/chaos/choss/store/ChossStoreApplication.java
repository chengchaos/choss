package luxe.chaos.choss.store;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ChossStoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChossStoreApplication.class, args);
	}

}
