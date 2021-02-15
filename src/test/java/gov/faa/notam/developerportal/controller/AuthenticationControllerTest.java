package gov.faa.notam.developerportal.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import gov.faa.notam.developerportal.exception.ApiException;
import gov.faa.notam.developerportal.model.api.LoginRequest;
import gov.faa.notam.developerportal.model.api.LoginResponse;
import gov.faa.notam.developerportal.service.AuthenticationService;

/**
 * Test class for AuthenticationController
 * @author AjithCK
 *
 */
@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
public class AuthenticationControllerTest
{

	/**
	 * Class under test
	 */
	AuthenticationController authenticationController;
	
	/**
	 * Mocking AuthenticationService
	 */
	@Mock
	private  AuthenticationService authenticationService;
	
	/**
	 * Initialize AuthenticationController
	 */
	@BeforeEach void setupEach() 
 	{ 
		authenticationController = new AuthenticationController(authenticationService);
 	}
	
	/**
	 * Method to test AuthenticationController.login method
	 */
	@DisplayName("Test AuthenticationController.login()")
	@Test
	public void loginTest()
	{
		LoginRequest  loginRequest  = new LoginRequest();
		LoginResponse loginResponse = new LoginResponse("1234");
		
		try {
			Mockito.when(authenticationService.login(loginRequest)).thenReturn(loginResponse);
			LoginResponse response = authenticationController.login(loginRequest);
			
			Assertions.assertEquals(response, loginResponse);
		} catch (ApiException e) 
		{
			e.printStackTrace();
		}
	}
}
