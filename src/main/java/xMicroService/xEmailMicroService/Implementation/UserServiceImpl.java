package xMicroService.xEmailMicroService.Implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import xMicroService.xEmailMicroService.Entity.UserWriteAccessData;
import xMicroService.xEmailMicroService.Entity.WriteAccessConfirmation;
import xMicroService.xEmailMicroService.Entity.Confirmation;
import xMicroService.xEmailMicroService.Entity.User;
import xMicroService.xEmailMicroService.Repository.ConfirmationRepository;
import xMicroService.xEmailMicroService.Repository.UserRepository;
import xMicroService.xEmailMicroService.Repository.UserWriteAccessDataRepo;
import xMicroService.xEmailMicroService.Repository.WriteAccessConfirmationRepo;
import xMicroService.xEmailMicroService.Service.EmailService;
import xMicroService.xEmailMicroService.Service.UserService;

@Service
public class UserServiceImpl implements UserService {
	
	private String defaultToEmail = "vishchy111@gmail.com";
	//private String defaultToEmail = "djsvaids@gmail.com";

	// DI
	private final UserRepository userRepository;
	private final ConfirmationRepository confirmationRepository;
	private final EmailService emailService;
	
	private final UserWriteAccessDataRepo userWriteAccessDataRepo;
	private final WriteAccessConfirmationRepo writeAccessConfirmationRepo;
	
	
	@Autowired
	public UserServiceImpl(UserRepository userRepository, ConfirmationRepository confirmationRepository,
			EmailService emailService, UserWriteAccessDataRepo userWriteAccessDataRepo,
			WriteAccessConfirmationRepo writeAccessConfirmationRepo) {
		super();
		this.userRepository = userRepository;
		this.confirmationRepository = confirmationRepository;
		this.emailService = emailService;
		this.userWriteAccessDataRepo = userWriteAccessDataRepo;
		this.writeAccessConfirmationRepo = writeAccessConfirmationRepo;
	}




	@Override
	public User saveUser(User user) {
		// save the user to the dB
		if(this.userRepository.existsByEmail(user.getEmail())) {
			// checks if user already exists
			throw new RuntimeException("Email already exists!");
		}
		
		// before we save, we first disable the user
		user.setConfirmed(false); // setEnabled
		
		// then we'll save the user
		this.userRepository.save(user);
		
		// we'll create the confirmation using the constructor we defined
		Confirmation confirmation = new Confirmation(user);
		
		// we'll save the confirmation and token
		this.confirmationRepository.save(confirmation);
		// confirmation obj has id, token, date/time and user with user_id refering the user table
		
		// TODO... Send mail to the user for verification and enabling/confirming the user...
		
		//this.emailService.sendSimpleMailMessage(user.getName(), user.getEmail(), confirmation.getToken());
		//this.emailService.sendMimeMessageWithAttachments(user.getName(), user.getEmail(), confirmation.getToken());
		//this.emailService.sendMimeMessageWithEmbeddedFiles(user.getName(), user.getEmail(), confirmation.getToken());
		//this.emailService.sendHtmlEmail(user.getName(), user.getEmail(), confirmation.getToken());
		this.emailService.sendHtmlEmailWithEmbeddedFiles(user.getName(), user.getEmail(), confirmation.getToken());
		return user;
	}

	@Override
	public Boolean verifyToken(String token) {
		Confirmation confirmation = this.confirmationRepository.findByToken(token);
		
		// finding user with the token
		User user = userRepository.findByEmailIgnoreCase(confirmation.getUser().getEmail());
		// this is needed for enabling the user that's why we had to call the user again to have the user 
		// enabled/confirmed on the user side as well
		user.setConfirmed(true);
		this.userRepository.save(user);
		
		// then delete the confirmation corresponding to that user from the confirmation table
		//this.confirmationRepository.delete(confirmation); like this
		return true;
	}
	
	// we could more checks here to confirm that token is not null for that user etc
	
	
	
	

	// Write access for another outside app...
	@Override
	public void writeAcces(UserWriteAccessData userData) {
		
		System.out.println("User Service Impl");
		
		if(this.userWriteAccessDataRepo.existsByEmail(userData.getEmail())) {
			throw new RuntimeException("Email Already Exists in the Database");
		}
		
		// saving data
		this.userWriteAccessDataRepo.save(userData); 
		
		// generating token and saving it as well
		WriteAccessConfirmation confirmation = new WriteAccessConfirmation(userData);
		this.writeAccessConfirmationRepo.save(confirmation);
		
		
		System.out.println("User Details Saved SuccessFully");
		
		// send email
		this.emailService.sendSimpleWriteAccessMail(defaultToEmail, userData.getFirstName() + " " + userData.getLastName(),
														userData.getEmail(), userData.getPhoneNumber(), confirmation.getToken(), defaultToEmail);
		System.out.println("Email Sent successfully");
		
	}


	//verifyWriteAccessToken method

	@Override
	public Map<String, String> confirmWriteToken(String token) {
		
		Optional<WriteAccessConfirmation> confirmationOpt = this.writeAccessConfirmationRepo.findByToken(token);
		
		if(confirmationOpt.isEmpty()) {
			System.out.println("No Such token exists");
			return null;
		}
		
		WriteAccessConfirmation confirmation = confirmationOpt.get();
		
		UserWriteAccessData user = confirmation.getUserAccessData();
		
		Map<String,String> emails = new HashMap<String, String>();
		
		//Integer userId = user.getId(); // will grant permission to the user who requested the write permission
		String userEmail = user.getEmail(); // this will grant permission to the email request by the user
		System.out.println(userEmail + "User Email inside confirmWriteToken in UserServieImpl");
		
		String userOwnEmail = user.getUserOwnEmail();
		
		emails.put("userEmail", userEmail);
		emails.put("userOwnEmail", userOwnEmail);
		
		// Check if its older than 30 mins with fetched confiramtion
		LocalDateTime craetedAt = confirmation.getCreatedDate();
		LocalDateTime now = LocalDateTime.now();
		
		Duration timeLapsed = Duration.between(craetedAt, now);
		
		if(timeLapsed.toMinutes() > 30) {
			this.userWriteAccessDataRepo.delete(user);
			this.writeAccessConfirmationRepo.delete(confirmation);
			emails = null;
			System.out.println("Token and User Deleted Successfully");
		}
		
		System.out.println("UserServiceImpl methods executed successfully now forwording to controller");
		return emails;
	}
	
	
	@Override
	public void accessGranted(String email) { // email - to
		UserWriteAccessData user =  this.userWriteAccessDataRepo.findByUserOwnEmail(email);
		String fullName = user.getFirstName() + " " + user.getLastName();
		this.emailService.adminResponse(fullName, email); // email -to
		System.out.println("Email Sent successfully");
	}
	
}
