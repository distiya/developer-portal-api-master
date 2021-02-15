package gov.faa.notam.developerportal.service.impl.test;

import gov.faa.notam.developerportal.service.impl.EmailServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * Test class for EmailServiceImpl
 * @author AjithCK
 *
 */
@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
public class EmailServiceImplTest 
{

	/**
	 * Class under test
	 */
	EmailServiceImpl emailServiceImpl;
	
	/**
	 * Mocking JavaMailSender
	 */
	@Mock
	private JavaMailSender javaMailSender;

	/**
	 * Mocking JavaMailSender
	 */
	@Mock
    private SpringTemplateEngine springTemplateEngine;
	
	/**
	 * Initialize EmailServiceImpl and setting values in private fields.
	 */
	@BeforeEach
    void setupEach()
	{
		emailServiceImpl = new EmailServiceImpl(javaMailSender, springTemplateEngine);
		ReflectionTestUtils.setField(emailServiceImpl, "from", "test@test.com");
		ReflectionTestUtils.setField(emailServiceImpl, "feedbackEmail", "test@test.com");
	}
	
	/**
	 * Method to test EmailServiceImpl.sendConfirmationMail method
	 */
	@DisplayName("Test EmailServiceImpl.sendConfirmationMail()")
	@Test
	public void sendConfirmationMailTest()
	{
		Properties  properties  = new Properties ();
		Session     session     = Session.getInstance(properties);
		MimeMessage message     = new MimeMessage(session);
		
		Context context = new Context();
	    context.setVariable("userId", 123L);
	    context.setVariable("email", "test@test.com");
	    context.setVariable("code", "Hello");
	        
		Mockito.when(javaMailSender.createMimeMessage()).thenReturn(message);
		Mockito.when(springTemplateEngine.process(Mockito.any(String.class), Mockito.any(Context.class))).thenReturn("Hello");
		
		emailServiceImpl.sendConfirmationMail(123L, "test@test.com", "Hello");
	}
	
	/**
	 * Method to test EmailServiceImpl.sendConfirmationMail method.
	 * This method checks whether exception is thrown when invalid email is passed in in put.
	 */
	@DisplayName("Test EmailServiceImpl.sendConfirmationMail() Exception")
	@Test
	public void sendConfirmationMail1Test()
	{
		Properties  properties  = new Properties ();
		Session     session     = Session.getInstance(properties);
		MimeMessage message     = new MimeMessage(session);
		
		Context context = new Context();
	    context.setVariable("userId", 123L);
	    context.setVariable("email", "test@test.com");
	    context.setVariable("code", "Hello");
	        
		Mockito.when(javaMailSender.createMimeMessage()).thenReturn(message);
		Mockito.when(springTemplateEngine.process(Mockito.any(String.class), Mockito.any(Context.class))).thenReturn("Hello");
		
		emailServiceImpl.sendConfirmationMail(123L, "@test.com", "Hello");
	}
	
	/**
	 * Method to test EmailServiceImpl.sendPasswordResetMail method
	 */
	@DisplayName("Test EmailServiceImpl.sendPasswordResetMail()")
	@Test
	public void sendPasswordResetMailTest()
	{
		Properties  properties  = new Properties ();
		Session     session     = Session.getInstance(properties);
		MimeMessage message     = new MimeMessage(session);
		
		Context context = new Context();
	    context.setVariable("userId", 123L);
	    context.setVariable("email", "test@test.com");
	    context.setVariable("code", "Hello"); 
	        
	        
		Mockito.when(javaMailSender.createMimeMessage()).thenReturn(message);
		Mockito.when(springTemplateEngine.process(Mockito.any(String.class), Mockito.any(Context.class))).thenReturn("Hello");
		
		emailServiceImpl.sendPasswordResetMail(123L, "test@test.com", "Hello");
	}
	
	/**
	 * Method to test EmailServiceImpl.sendPasswordResetMail method.
	 * This method checks exception is thrown when invalid email is passed to input.
	 */
	@DisplayName("Test EmailServiceImpl.sendPasswordResetMail()- Exception test")
	@Test
	public void sendPasswordResetMail1Test()
	{
		Properties  properties  = new Properties ();
		Session     session     = Session.getInstance(properties);
		MimeMessage message     = new MimeMessage(session);
		
		 Context context = new Context();
	     context.setVariable("userId", 123L);
	     context.setVariable("email", "test@test.com");
	     context.setVariable("code", "Hello");
	        
	        
		Mockito.when(javaMailSender.createMimeMessage()).thenReturn(message);
		Mockito.when(springTemplateEngine.process(Mockito.any(String.class), Mockito.any(Context.class))).thenReturn("Hello");
		
		emailServiceImpl.sendPasswordResetMail(123L, "@test.com", "Hello");
		
	}
	
	/**
	 * Method to test EmailServiceImpl.sendFeedback method.
	 */
	@DisplayName("Test EmailServiceImpl.sendFeedback()")
	@Test
	public void sendFeedbackTest()
	{
		Properties  properties  = new Properties ();
		Session     session     = Session.getInstance(properties);
		MimeMessage message     = new MimeMessage(session);
		
		 Context context = new Context();
	     context.setVariable("userId", 123L);
	     context.setVariable("email", "test@test.com");
	     context.setVariable("code", "Hello");
	        
	        
		Mockito.when(javaMailSender.createMimeMessage()).thenReturn(message);
		Mockito.when(springTemplateEngine.process(Mockito.any(String.class), Mockito.any(Context.class))).thenReturn("Hello");
		
		emailServiceImpl.sendFeedback("test@test.com", "Hello");
	}
	
	/**
	 * Method to test EmailServiceImpl.sendFeedback method.
	 */
	@DisplayName("Test EmailServiceImpl.sendFeedback()")
	@Test
	public void sendFeedbackExceptionTest()
	{
		Properties  properties  = new Properties ();
		Session     session     = Session.getInstance(properties);
		MimeMessage message     = new MimeMessage(session);
		
		ReflectionTestUtils.setField(emailServiceImpl, "from", "@test.com");
		ReflectionTestUtils.setField(emailServiceImpl, "feedbackEmail", "@test.com");
		
		Context context = new Context();
	    context.setVariable("userId", 123L);
	    context.setVariable("email", "test@test.com");
	    context.setVariable("code", "Hello");
	        
	        
		Mockito.when(javaMailSender.createMimeMessage()).thenReturn(message);
		Mockito.when(springTemplateEngine.process(Mockito.any(String.class), Mockito.any(Context.class))).thenReturn("Hello");
		
		emailServiceImpl.sendFeedback("test@test.com", "Hello");
	}
	
}
