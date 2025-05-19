package xMicroService.xEmailMicroService.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import xMicroService.xEmailMicroService.Entity.WriteAccessConfirmation;

@Repository
public interface WriteAccessConfirmationRepo extends JpaRepository<WriteAccessConfirmation, Integer> {
	
	// Save and other common jpa provides
	
	Optional<WriteAccessConfirmation> findByToken(String token);
}
