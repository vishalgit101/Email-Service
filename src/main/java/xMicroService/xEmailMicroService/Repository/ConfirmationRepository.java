package xMicroService.xEmailMicroService.Repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import xMicroService.xEmailMicroService.Entity.Confirmation;


@Repository
public interface ConfirmationRepository extends JpaRepository<Confirmation, Long> {
	Confirmation findByToken(String token);
	// we'll give token and it'll return confirmation
}
