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
import gov.faa.notam.developerportal.model.api.CreateNotamApiTokenRequest;
import gov.faa.notam.developerportal.model.api.NotamApiTokenModel;
import gov.faa.notam.developerportal.model.api.SearchNotamApiTokenRequest;
import gov.faa.notam.developerportal.model.api.SearchResponse;
import gov.faa.notam.developerportal.model.api.UpdateNotamApiTokenRequest;
import gov.faa.notam.developerportal.model.entity.NotamApiToken;
import gov.faa.notam.developerportal.service.NotamApiTokenService;

/**
 * Test class for NotamApiTokenController
 * @author AjithCK
 *
 */
@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
public class NotammApiTokenControllerTest {

	/**
	 * Class under test
	 */
	NotamApiTokenController notamApiTokenController;
	
	/**
	 * mocking NotamApiTokenService
	 */
	@Mock
	NotamApiTokenService notamApiTokenService;
	
	
 	/**
 	 * Initialize NotamApiTokenController
 	 */
 	@BeforeEach void setupEach() 
 	{ 
 		notamApiTokenController = new 	NotamApiTokenController(notamApiTokenService); 
 	}
	
	
	
	/**
	 * Method to test NotamApiTokenController.deleteToken method.
	 */
	@DisplayName("Test NotamApiTokenController.deleteToken()")
	@Test
	public void deleteTokenTest()
	{
		try {
			notamApiTokenController.deleteToken(99L);
		} catch (ApiException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Method to test NotamApiTokenController.enableToken method.
	 */
	@DisplayName("Test NotamApiTokenController.enableToken()")
	@Test
	public void enableTokenTest()
	{
		try {
			notamApiTokenController.enableToken(99L);
		} catch (ApiException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Method to test NotamApiTokenController.disableToken method.
	 */
	@DisplayName("Test NotamApiTokenController.disableToken()")
	@Test
	public void disableTokenTest()
	{
		try {
			notamApiTokenController.disableToken(99L);
		} catch (ApiException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Method to test NotamApiTokenController.updateToken method.
	 */
	@DisplayName("Test NotamApiTokenController.updateToken()")
	@Test
	public void updateTokenTest()
	{
		try {
			UpdateNotamApiTokenRequest request = new UpdateNotamApiTokenRequest();
			notamApiTokenController.updateToken(99L, request);
		} catch (ApiException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Method to test NotamApiTokenController.createToken method.
	 */
	@DisplayName("Test NotamApiTokenController.createToken()")
	@Test
	public void createTokenTest()
	{
		NotamApiToken      token              = new NotamApiToken();
		NotamApiTokenModel notamApiTokenModel = new NotamApiTokenModel(token);
		try {
			CreateNotamApiTokenRequest createNotamApiTokenRequest = new CreateNotamApiTokenRequest();
			Mockito.when(notamApiTokenService.createToken(createNotamApiTokenRequest)).
			thenReturn(notamApiTokenModel);
			
			NotamApiTokenModel returnModel = notamApiTokenController.createToken(createNotamApiTokenRequest);
			
			Assertions.assertEquals(returnModel, notamApiTokenModel);
			
		} catch (ApiException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Test method for NotamApiTokenController.searchToken method
	 */
	@DisplayName("Test NotamApiTokenController.searchToken()")
	@Test
	public void searchTokenTest()
	{
		SearchNotamApiTokenRequest         request  = new SearchNotamApiTokenRequest();
		SearchResponse<NotamApiTokenModel> response = new SearchResponse<>();
		
		
		try {
			Mockito.when(notamApiTokenService.searchToken(request)).
			thenReturn(response);
			
			SearchResponse<NotamApiTokenModel> returnValue = notamApiTokenController.searchToken(request);
			
			Assertions.assertEquals(returnValue, response);
		} catch (ApiException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Test method for NotamApiTokenController.getToken method.
	 */
	@DisplayName("Test NotamApiTokenController.getToken()")
	@Test
	public void getTokenTest()
	{
		NotamApiToken      token = new NotamApiToken();
		NotamApiTokenModel model = new NotamApiTokenModel(token);
		try {
			
			Mockito.when(notamApiTokenService.getToken(123L)).
			thenReturn(model);
			
			NotamApiTokenModel returnValue = notamApiTokenController.getToken(123L);
			
			Assertions.assertEquals(returnValue, model);
			
		} catch (ApiException e) {
			e.printStackTrace();
		}
	}
}
