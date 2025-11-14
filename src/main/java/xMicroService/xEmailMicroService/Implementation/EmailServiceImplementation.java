package xMicroService.xEmailMicroService.Implementation;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import jakarta.mail.BodyPart;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import xMicroService.xEmailMicroService.Service.EmailService;
import xMicroService.xEmailMicroService.utils.EmailUtils;
import java.io.*;
import java.util.Map;
import jakarta.activation.*;
@Service
public class EmailServiceImplementation implements EmailService {
	// values will be passed when this bean is created
	@Value("${spring.mail.verify.host}")
	private String host;
	
	@Value("${spring.blogAppUrl}")
	private String blogAppHost;
	
	@Value("${spring.mail.username}")
	private String fromEmail;
	
	//DI
	private final JavaMailSender emailSender;
	
	private final TemplateEngine templateEngine;
	
	public EmailServiceImplementation(JavaMailSender emailSender, TemplateEngine templateEngine) {
		super();
		this.emailSender = emailSender;
		this.templateEngine = templateEngine;
	}

	@Override
	@Async
	public void sendSimpleMailMessage(String name, String to, String token) {
			
		try {
			SimpleMailMessage message = new SimpleMailMessage();
			message.setSubject("New User Account Verification");
			message.setFrom(fromEmail);
			message.setTo(to);
			//message.setText("Hey, this is working... hahahhaha");
			message.setText(EmailUtils.getEmailMessage(name, this.host, token));
			emailSender.send(message);	
		}catch (Exception e) {
			System.out.println(e.getMessage());
			throw new RuntimeException(e.getMessage());
		}
		
	}

	@Override
	@Async
	public void sendMimeMessageWithAttachments(String name, String to, String token) {
		try {
			MimeMessage message = this.emailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
			helper.setPriority(1);
			helper.setSubject("New User Account Verification");
			helper.setFrom(fromEmail);
			helper.setTo(to);
			//message.setText("Hey, this is working... hahahhaha");
			helper.setText(EmailUtils.getEmailMessage(name, this.host, token));
			
			// Add Attachments
			FileSystemResource vishal = new FileSystemResource(new File(System.getProperty("user.home") + "/Desktop/EmailFiles/Image2.jpeg"));
			FileSystemResource gojo = new FileSystemResource(new File(System.getProperty("user.home") + "/Desktop/EmailFiles/Image.jpg"));
			FileSystemResource pdfFile = new FileSystemResource(new File(System.getProperty("user.home") + "/Desktop/EmailFiles/ppt.pdf"));
			
			helper.addAttachment(vishal.getFilename(), vishal);
			helper.addAttachment(gojo.getFilename(), gojo);
			helper.addAttachment(pdfFile.getFilename(), pdfFile);
			emailSender.send(message);	
		}catch (Exception e) {
			System.out.println(e.getMessage());
			throw new RuntimeException(e.getMessage());
		}
		
	}

	@Override
	@Async
	public void sendMimeMessageWithEmbeddedFiles(String name, String to, String token) {
		
		try {
			MimeMessage message = this.emailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
			helper.setPriority(1);
			helper.setSubject("New User Account Verification");
			helper.setFrom(fromEmail);
			helper.setTo(to);
			//message.setText("Hey, this is working... hahahhaha");
			helper.setText(EmailUtils.getEmailMessage(name, this.host, token));
			
			// Add Attachments
			FileSystemResource vishal = new FileSystemResource(new File(System.getProperty("user.home") + "/Desktop/EmailFiles/Image2.jpeg"));
			FileSystemResource gojo = new FileSystemResource(new File(System.getProperty("user.home") + "/Desktop/EmailFiles/Image.jpg"));
			FileSystemResource pdfFile = new FileSystemResource(new File(System.getProperty("user.home") + "/Desktop/EmailFiles/ppt.pdf"));
			
			helper.addInline( "<" + vishal.getFilename() + ">", vishal);
			helper.addInline("<" + gojo.getFilename() + ">", gojo);
			helper.addInline( "<" + pdfFile.getFilename() + ">", pdfFile);
			emailSender.send(message);	
		}catch (Exception e) {
			System.out.println(e.getMessage());
			throw new RuntimeException(e.getMessage());
		}
		
		
	}


	@Override
	@Async
	public void sendHtmlEmail(String name, String to, String token) {
			// need to use context...Thymeleaf Standalone Engine (e.g. Emails, PDFs, Reports)
		
		try {
			// Thymeleaf context
			// these context fields will be used in the Template file, as we can check
			Context context = new Context();
			context.setVariable("name", name);
			context.setVariable("url", EmailUtils.getVerificationUrl(this.host, token));
			
			// Can Also use Map for adding values to the context
			//context.setVariables(Map.of("name",name, "url", EmailUtils.getVerificationUrl(to, token)));
			
			String text = this.templateEngine.process("emailtemplate", context); // whole html template as a string
			
			MimeMessage message = this.emailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
			helper.setPriority(1);
			helper.setSubject("New User Account Verification");
			helper.setFrom(fromEmail);
			helper.setTo(to);
			
			//message.setText("Hey, this is working... hahahhaha");
			//helper.setText(EmailUtils.getEmailMessage(name, this.host, token));
			
			helper.setText(text, true); // true is needed for sending rich HTML email
			
			// Add Attachments
			FileSystemResource vishal = new FileSystemResource(new File(System.getProperty("user.home") + "/Desktop/EmailFiles/Image2.jpeg"));
			FileSystemResource gojo = new FileSystemResource(new File(System.getProperty("user.home") + "/Desktop/EmailFiles/Image.jpg"));
			FileSystemResource pdfFile = new FileSystemResource(new File(System.getProperty("user.home") + "/Desktop/EmailFiles/ppt.pdf"));
			
			helper.addAttachment( vishal.getFilename(), vishal);
			helper.addAttachment(gojo.getFilename(), gojo);
			helper.addAttachment( pdfFile.getFilename(), pdfFile);

			emailSender.send(message);	
		}catch (Exception e) {
			System.out.println(e.getMessage());
			throw new RuntimeException(e.getMessage());
		}
		
	}

	@Override
	@Async
	public void sendHtmlEmailWithEmbeddedFiles(String name, String to, String token) {
		
		try {
			Context context = new Context();
			context.setVariable("name", name);
			context.setVariable("url", EmailUtils.getVerificationUrl(this.host, token));
			
			// Can Also use Map for adding values to the context
			//context.setVariables(Map.of("name",name, "url", EmailUtils.getVerificationUrl(to, token)));
			
			String text = this.templateEngine.process("emailtemplate", context); // whole html template as a string that has the name and url
			
			MimeMessage message = this.emailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
			helper.setPriority(1);
			helper.setSubject("New User Account Verification");
			helper.setFrom(fromEmail);
			helper.setTo(to);
			
			//helper.setText(text, true); // true is needed for sending rich HTML emails
			
			// Add HTML email body
			MimeMultipart mimeMultipart = new MimeMultipart("related");
			
			// First part - message
			BodyPart messageBodyPart1 = new MimeBodyPart(); // BodyPart is an Abstract class and MimeBodyPart extends BodyPart
															// and we can define the instance of child class while refering the parent class
			messageBodyPart1.setContent(text, "text/html");
			mimeMultipart.addBodyPart(messageBodyPart1);
			
			//Part 0- Adding logo
			BodyPart messageBodyPart0 = new MimeBodyPart();
			DataSource dataSourceLogo = new FileDataSource(System.getProperty("user.home") + "/Desktop/EmailFiles/Image2.jpeg");
			messageBodyPart0.setDataHandler(new DataHandler(dataSourceLogo));
			messageBodyPart0.setHeader("Content-ID", "logo");
			mimeMultipart.addBodyPart(messageBodyPart0);
			
			// Second Part - Image
			BodyPart messageBodyPart2 = new MimeBodyPart();
			DataSource dataSource = new FileDataSource(System.getProperty("user.home") + "/Desktop/EmailFiles/Image3.png");
			// passing that image into the body
			messageBodyPart2.setDataHandler(new DataHandler(dataSource));
			
			messageBodyPart2.setHeader("Content-ID", "image"); // Content-ID existing Header name
			
			// Now add  second part to the multipart
			mimeMultipart.addBodyPart(messageBodyPart2); // appends to the list of existing body parts...its multipart baby!
			
			// Now add the multipart to the message
			message.setContent(mimeMultipart);
			
			emailSender.send(message);	
		}catch (Exception e) {
			System.out.println(e.getMessage());
			throw new RuntimeException(e.getMessage());
		}
		
	}

	@Override
	@Async
	public void sendSimpleWriteAccessMail(String devName, String senderName, String accessEmail, String phoneNumber,
			String token, String to) {
		
		/*try {
			SimpleMailMessage message = new SimpleMailMessage();
			message.setSubject("New User Account Verification");
			message.setFrom(fromEmail);
			message.setTo(to);
			//message.setText("Hey, this is working... hahahhaha");
			message.setText(EmailUtils.getEmailMessage(name, this.host, token));
			emailSender.send(message);	
		}catch (Exception e) {
			System.out.println(e.getMessage());
			throw new RuntimeException(e.getMessage());
		}*/
		
		try {
			SimpleMailMessage message = new SimpleMailMessage();
			message.setSubject("Requesting Permission for Writing Blogs");
			message.setFrom(this.fromEmail);
			message.setTo(to);
			message.setText(EmailUtils.getWriteAccessMessage(devName, senderName, phoneNumber, accessEmail, this.blogAppHost, token));
			emailSender.send(message);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			throw new RuntimeException(e.getMessage());
		}
		
		
	}

	@Override
	@Async
	public void adminResponse(String userName, String to) { // to user email
		try {
			Context context = new Context();
			context.setVariable("userName", userName);
			
			context.setVariable("message", EmailUtils.sendAdminResponse(to));
			context.setVariable("btnName", "BlogHub");
			
			context.setVariable("url", EmailUtils.sendAdminResponseUrl(host));
			
			String text = this.templateEngine.process("emailTemplate", context);
			
			MimeMessage message = this.emailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
			helper.setPriority(1);
			helper.setSubject("Request Accepted for Wriring blogs!");
			helper.setFrom(fromEmail);
			helper.setTo(to);
			
			MimeMultipart mimeMultipart = new MimeMultipart("related");
			
			//BodyPart 1 - Message
			BodyPart messageBodyPart0 = new MimeBodyPart();
			messageBodyPart0.setContent(text, "text/html");
			mimeMultipart.addBodyPart(messageBodyPart0);
			
			// BodyPart 2 Adding Logo
			BodyPart logoBodyPart = new MimeBodyPart();
			DataSource dataSourceLogo = new FileDataSource(System.getProperty("user.home") + "/Desktop/EmailFiles/Image2.jpeg");
			logoBodyPart.setDataHandler(new DataHandler(dataSourceLogo));
			logoBodyPart.setHeader("Content-ID", "logo");
			mimeMultipart.addBodyPart(logoBodyPart);
			
			//Body Part 3 Adding Image
			BodyPart imageBodyPart = new MimeBodyPart();
			DataSource dataSourceImage = new FileDataSource(System.getProperty("user.home")+ "/Desktop/EmailFiles/Image3.png");
			imageBodyPart.setDataHandler(new DataHandler(dataSourceImage));
			imageBodyPart.setHeader("Content-ID", "image");
			mimeMultipart.addBodyPart(imageBodyPart);
			
			// Now add the multipart to the message
			message.setContent(mimeMultipart);
			emailSender.send(message);
			
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

}
