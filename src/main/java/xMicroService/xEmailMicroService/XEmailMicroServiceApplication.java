package xMicroService.xEmailMicroService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class XEmailMicroServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(XEmailMicroServiceApplication.class, args);
	}

}
