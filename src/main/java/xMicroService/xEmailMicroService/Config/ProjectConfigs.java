package xMicroService.xEmailMicroService.Config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "xMicroService.xEmailMicroService")
public class ProjectConfigs {
}
