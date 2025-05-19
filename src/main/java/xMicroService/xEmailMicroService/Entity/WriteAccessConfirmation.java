package xMicroService.xEmailMicroService.Entity;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "write_access_confirmation")
public class WriteAccessConfirmation {
	
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	private String token; // key, we'll send this to user to confirm who they are
	
	private LocalDateTime createdDate; // annotations for temporal and craeted
	
	@OneToOne(targetEntity = UserWriteAccessData.class, fetch = FetchType.EAGER)
	@JoinColumn(name ="user_id", nullable = false)
	private UserWriteAccessData userAccessData;
	
	public WriteAccessConfirmation(UserWriteAccessData userAccessData) {
		this.userAccessData = userAccessData;
		this.createdDate = LocalDateTime.now(); // date and time for when confirmation will be created
		this.token = UUID.randomUUID().toString(); // randomly generated token
	}

	public WriteAccessConfirmation(long id, String token, LocalDateTime createdDate,
			UserWriteAccessData userAccessData) {
		super();
		this.id = id;
		this.token = token;
		this.createdDate = createdDate;
		this.userAccessData = userAccessData;
	}

	public WriteAccessConfirmation() {
		super();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}

	public UserWriteAccessData getUserAccessData() {
		return userAccessData;
	}

	public void setUserAccessData(UserWriteAccessData userAccessData) {
		this.userAccessData = userAccessData;
	}
	
	
	
}
