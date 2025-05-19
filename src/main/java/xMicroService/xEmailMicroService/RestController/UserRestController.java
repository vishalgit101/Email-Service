package xMicroService.xEmailMicroService.RestController;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import xMicroService.xEmailMicroService.Entity.UserWriteAccessData;
import xMicroService.xEmailMicroService.Entity.User;
import xMicroService.xEmailMicroService.Model.HttpResponse;
import xMicroService.xEmailMicroService.Service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserRestController {
	// DI
	private final UserService userService;

	public UserRestController(UserService userService) {
		super();
		this.userService = userService;
	}
	
	@PostMapping
	public ResponseEntity<HttpResponse> createUser(@RequestBody User user, HttpServletRequest request){
		System.out.println(user);
		User newUser = userService.saveUser(user);

        HttpResponse response = new HttpResponse(
                LocalDateTime.now().toString(),
                HttpStatus.CREATED.value(),
                HttpStatus.CREATED,
                "User created successfully",
                "User created with ID: " + newUser.getId(),
                request.getRequestURI(),
                request.getMethod(),
                Map.of("user", newUser)
        );

        return ResponseEntity.created(URI.create("/api/users/" + newUser.getId()))
                             .body(response);
	}
	
	// method for verifying the token- GetMapping
	@GetMapping
	public ResponseEntity<HttpResponse> confirmUserAccount(@RequestParam("token") String token, HttpServletRequest request){
		
		Boolean isSuccess = userService.verifyToken(token);

        HttpResponse response = new HttpResponse(
                LocalDateTime.now().toString(),
                HttpStatus.OK.value(),
                HttpStatus.OK,
                "Account verified",
                "Account verified Successfully ",
                request.getRequestURI(),
                request.getMethod(),
                Map.of("Success", isSuccess)
        );

        return ResponseEntity.ok()
                             .body(response);
	}
	
	@PostMapping("/write-access")
	public ResponseEntity<Void> giveWriteAccess(@RequestHeader String requestId, @RequestBody Map<String, String> payload) {
		System.out.println("Write Access Controller");
		System.out.println("BAckend COntroller Payload:" +payload);
		System.out.println("userId: " + payload.get("userId"));

		int userId =  Integer.parseInt(payload.get("userId"));
		System.out.println("Parsed userId:" + userId);

		String firstName = payload.get("firstName");
		String lastName = payload.get("lastName");
		String email = payload.get("email");
		String phoneNumber =  payload.get("phoneNumber");
		String description = payload.get("des");
		
		String userOwnEmail = payload.get("userOwnEmail");
		
		
		UserWriteAccessData userData = new UserWriteAccessData(userId, firstName, lastName, email, phoneNumber, description, userOwnEmail);
		// Now Data about the user will get saved 
		this.userService.writeAcces(userData);
		return ResponseEntity.status(HttpStatus.OK).build();
	}
	
	/*@GetMapping("/write-access/confirmation")
	public ResponseEntity<Void> confirmWriteRequest(@RequestParam("token") String token){
		
		// Check if the request is not older than 30mins and delete request user and confirmation if older than 30min
		
		
		// Under 30min
		
		//confirm token if valid give access for writing blogs
		
		// but write access can be granted from the other app.... which is blog app
		
		//open feign request to blog post app with only userId as parameter
		
		
		
		return null;
	}*/
	
	@GetMapping("/write-access/confirmation")
	public ResponseEntity<Map<String, String>> confirmWriteRequest( @RequestHeader String requestId ,@RequestParam("token") String token){
		
		System.out.println("Request hit the write acess controller");
		
		// New flow
		// Blogbackend makes call for the token confirmation
		
		// confirms token corresponding to a specific user
		Map<String,String> emails =  this.userService.confirmWriteToken(token); // deletes if older than 30 mins
		String userAccessEmail = emails.get("userEmail");
		String userOwnEmail = emails.get("userOwnEmail");
		
		System.out.println(userAccessEmail + "Write access api in userrestcontroller");
		
		if(userAccessEmail == null) { 
			throw new RuntimeException("Request didn't exist or Request got Expired");
		}
		
		System.out.println("Controller in email service successfull as well...fowrwardinig req to blog app");
		// after confirming sends ok as response and delegates the task of granting permission to blogpost app
		Map<String, String> body = Map.of("userAccessEmail", userAccessEmail, "userOwnEmail", userOwnEmail);
		
		return ResponseEntity.status(HttpStatus.OK).body(body);
		
	}
	
	@PostMapping("/access-granted")
	public ResponseEntity<Void> accessGranted(@RequestHeader String requestId, @RequestBody Map<String, String> body){
		String userOwnEmail = body.get("userOwnEmail");
		
		this.userService.accessGranted(userOwnEmail);
		
		return ResponseEntity.status(HttpStatus.OK).build();
	}
	
}
