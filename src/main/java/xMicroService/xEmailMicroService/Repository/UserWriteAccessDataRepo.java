package xMicroService.xEmailMicroService.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import xMicroService.xEmailMicroService.Entity.UserWriteAccessData;

@Repository
public interface UserWriteAccessDataRepo extends JpaRepository<UserWriteAccessData, Integer > {
	
	// Save, update, delete, and findById are already provided by JpaRepository.
	
	@Query("FROM UserWriteAccessData Where LOWER(email) = LOWER(:email)")
	UserWriteAccessData findByEmailIgnoreCase(@Param("email") String email); // to confirm user dont exists already
	
	/*@Query("SELECT u FROM UserWriteAccessData u Where u.id =:userId")
	UserWriteAccessData findById(@Param("userId") int userId);*/
	
	Optional<UserWriteAccessData> findById(Integer id); // no need for this either, jpa provides it as well

	
	/*@Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM UserWriteAccessData u WHERE u.id = :userId")
	Boolean existsById(@Param("userId") int userId);*/
	
	boolean existsById(Integer id); // no need for this either, jpa provides it as well
	
	Boolean existsByEmail(String email);
	
	@Query("FROM UserWriteAccessData where LOWER(userOwnEmail)=LOWER(:theData)")
	UserWriteAccessData findByUserOwnEmail(@Param("theData") String email);
}
