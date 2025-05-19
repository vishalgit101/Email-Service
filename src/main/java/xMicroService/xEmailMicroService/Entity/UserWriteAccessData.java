package xMicroService.xEmailMicroService.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="user_write_access_data")
public class UserWriteAccessData {
	@Id
	private int id; // id will come from the 
	
	private String firstName;
	private String lastName;
	private String email; // Access email
	private String userOwnEmail;
	private String phoneNumber;
	private String description;
	
	public UserWriteAccessData(int id, String firstName, String lastName, String email, String phoneNumber,
			String description, String userOwnEmail) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.description = description;
		this.userOwnEmail =userOwnEmail;
	}
	
	
	
	public String getUserOwnEmail() {
		return userOwnEmail;
	}



	public void setUserOwnEmail(String userOwnEmail) {
		this.userOwnEmail = userOwnEmail;
	}



	public UserWriteAccessData() {
		super();
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	
}
