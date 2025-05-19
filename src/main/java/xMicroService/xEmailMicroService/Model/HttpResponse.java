package xMicroService.xEmailMicroService.Model;

import java.util.Map;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonInclude;



//@Data
//@SuperBuilder no lombok
@JsonInclude(JsonInclude.Include.NON_DEFAULT) // values (like null, 0, false) are excluded
public class HttpResponse {
	protected String timeStamp;
	protected int statusCode;
	protected HttpStatus status;
	protected String message;
	protected String DeveloperMessage;
	protected String path; // path where the req was initiated on
	protected String requestMethod; // for sending that back as well, Method GET, POST which was used
	protected Map<?,?> data;
	
	
	public HttpResponse(String timeStamp, int statusCode, HttpStatus status, String message, String developerMessage,
			String path, String requestMethod, Map<?, ?> data) {
		super();
		this.timeStamp = timeStamp;
		this.statusCode = statusCode;
		this.status = status;
		this.message = message;
		this.DeveloperMessage = developerMessage;
		this.path = path;
		this.requestMethod = requestMethod;
		this.data = data;
	}


	public HttpResponse() {
		super();
	}


	public String getTimeStamp() {
		return timeStamp;
	}


	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}


	public int getStatusCode() {
		return statusCode;
	}


	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}


	public HttpStatus getStatus() {
		return status;
	}


	public void setStatus(HttpStatus status) {
		this.status = status;
	}


	public String getMessage() {
		return message;
	}


	public void setMessage(String message) {
		this.message = message;
	}


	public String getDeveloperMessage() {
		return DeveloperMessage;
	}


	public void setDeveloperMessage(String developerMessage) {
		DeveloperMessage = developerMessage;
	}


	public String getPath() {
		return path;
	}


	public void setPath(String path) {
		this.path = path;
	}


	public String getRequestMethod() {
		return requestMethod;
	}


	public void setRequestMethod(String requestMethod) {
		this.requestMethod = requestMethod;
	}


	public Map<?, ?> getData() {
		return data;
	}


	public void setData(Map<?, ?> data) {
		this.data = data;
	}


	@Override
	public String toString() {
		return "HttpResponse [timeStamp=" + timeStamp + ", statusCode=" + statusCode + ", status=" + status
				+ ", message=" + message + ", DeveloperMessage=" + DeveloperMessage + ", path=" + path
				+ ", requestMethod=" + requestMethod + ", data=" + data + "]";
	}
	
	
	
}
