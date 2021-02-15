package gov.faa.notam.developerportal.service.impl.test;

import gov.faa.notam.developerportal.exception.ApiException;
import gov.faa.notam.developerportal.security.SecurityUtil;
import gov.faa.notam.developerportal.service.EmailService;
import gov.faa.notam.developerportal.service.impl.FeedbackServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

/**
 * Test class for FeedbackServiceImpl
 * @author AjithCK
 *
 */
@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
public class FeedbackServiceImplTest
{

	/**
	 * Class under test
	 */
	FeedbackServiceImpl feedbackServiceImpl;
	
	/**
	 * Mocking EmailService
	 */
	@Mock
	private  EmailService emailService;
	
	/**
	 * Initialize FeedbackServiceImpl
	 */
	@BeforeEach
    void setupEach()
	{
		feedbackServiceImpl = new FeedbackServiceImpl(emailService);
		
	}
	
	/**
	 * Method to test FeedbackServiceImpl.sendFeedback method 
	 */
	@DisplayName("Test FeedbackServiceImpl.sendFeedback()")
	@Test
	public void sendFeedbackTest()
	{
		try (MockedStatic<gov.faa.notam.developerportal.security.SecurityUtil> mocked = Mockito
				.mockStatic(SecurityUtil.class))
		{
			mocked.when(SecurityUtil::getCurrentUserEmail).thenReturn(Optional.of("aaa@aaa"));
			try 
			{
				feedbackServiceImpl.sendFeedback("hello");
			} catch (ApiException e)
			{
				e.printStackTrace();
			}
		}
	}
}
