package gov.faa.notam.developerportal.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import gov.faa.notam.developerportal.exception.ApiException;
import gov.faa.notam.developerportal.model.api.SupportFeedbackRequest;
import gov.faa.notam.developerportal.service.FeedbackService;

/**
 * Test class for FeedbackController
 * @author AjithCK
 *
 */
@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
public class FeedbackControllerTest 
{
	
	/**
	 * Class under test
	 */
	FeedbackController feedbackController;
	
	/**
	 * Mocking FeedbackService
	 */
	@Mock
	private FeedbackService feedbackService;
	
	/**
	 * Initialize FeedbackController
	 */
	@BeforeEach void setupEach() 
 	{ 
		feedbackController = new FeedbackController(feedbackService);
 	}
	
	/**
	 * Method to test FeedbackController.sendFeedback method
	 */
	@DisplayName("Test FeedbackController.sendFeedback()")
	@Test
	public void sendFeedbackTest()
	{
		SupportFeedbackRequest supportFeedbackRequest = new SupportFeedbackRequest();
		supportFeedbackRequest.setComments("Test comment");
		try {
			feedbackController.sendFeedback(supportFeedbackRequest);
		} catch (ApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
