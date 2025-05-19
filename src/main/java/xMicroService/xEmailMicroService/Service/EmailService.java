package xMicroService.xEmailMicroService.Service;

public interface EmailService {
	// name - first name of the user
	// to- who you are sending mail to
	// token we'll send with the email so that user can click on and confirm their new account
	void sendSimpleMailMessage(String name, String to, String token);
	void sendMimeMessageWithAttachments(String name, String to, String token);
	void sendMimeMessageWithEmbeddedFiles(String name, String to, String token);
	void sendHtmlEmail(String name, String to, String token);
	void sendHtmlEmailWithEmbeddedFiles(String name, String to, String token);
	
	// For Sending mails for write access to the developer
	void sendSimpleWriteAccessMail(String devName, String senderName, String senderEmail, String phoneNumber, String token, String to);
	
	//Admin Response Not necesary as admin mail could get leaked but still as a proof of concept
	void adminResponse(String userName, String to );
}
