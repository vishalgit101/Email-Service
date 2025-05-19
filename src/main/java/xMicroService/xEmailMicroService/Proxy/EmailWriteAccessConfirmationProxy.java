package xMicroService.xEmailMicroService.Proxy;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;


// Not needed as now we only have blogbacken service exposed to the user only, and only that will confirm token by
//making call to this app...this class however is not needed
@FeignClient(name="emailsConfirmation", url = "${spring.blogAppUrl}")
public interface EmailWriteAccessConfirmationProxy {
	
	@PostMapping("/api/user/write-access/confirm")
	ResponseEntity<Void> confirmWriteAccess(int userId);
}
