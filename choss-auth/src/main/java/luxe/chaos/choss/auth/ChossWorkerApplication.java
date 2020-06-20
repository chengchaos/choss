package luxe.chaos.choss.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ChossWorkerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChossWorkerApplication.class, args);
	}

}
