package xMicroService.xEmailMicroService.Repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import xMicroService.xEmailMicroService.Entity.User;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	User findByEmailIgnoreCase(String email);
	
	Boolean existsByEmail(String email); 
	// checks if the same user exists
	// and going to return boolean for that
}
