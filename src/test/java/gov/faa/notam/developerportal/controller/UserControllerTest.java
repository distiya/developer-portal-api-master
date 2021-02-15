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
import gov.faa.notam.developerportal.model.api.ChangePasswordRequest;
import gov.faa.notam.developerportal.model.api.ForgotPasswordRequest;
import gov.faa.notam.developerportal.model.api.RegisterAdminRequest;
import gov.faa.notam.developerportal.model.api.RegisterUserRequest;
import gov.faa.notam.developerportal.model.api.ResetPasswordRequest;
import gov.faa.notam.developerportal.model.api.SearchUserRequest;
import gov.faa.notam.developerportal.model.api.UpdateUserRequest;
import gov.faa.notam.developerportal.service.UserService;

/**
 * Test class for UserController 
 * @author AjithCK
 *
 */
@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
public class UserControllerTest
{

	/**
	 * Class under test
	 */
	UserController userController;
	
	/**
	 * Mocking UserService.
	 */
	@Mock
	private  UserService userService;
	
	/**
	 * Initialize UserController
	 */
	@BeforeEach void setupEach() 
 	{ 
		userController = new UserController(userService);
 	}
	
	/**
	 * Method to test UserController.registerUser method
	 */
	@DisplayName("Test UserController.registerUser()")
	@Test
	public void registerUserTest()
	{
		try {
			userController.registerUser(new RegisterUserRequest());
		} catch (ApiException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Method to test UserController.registerAdmin method
	 */
	@DisplayName("Test UserController.registerAdmin()")
	@Test
	public void registerAdminTest()
	{
		try {
			userController.registerAdmin(new RegisterAdminRequest());
		} catch (ApiException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Method to test UserController.deleteAdmin method
	 */
	@DisplayName("Test UserController.deleteAdmin()")
	@Test
	public void deleteAdminTest()
	{
		try {
			userController.deleteAdmin(123L);
		} catch (ApiException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Method to test UserController.getUser method
	 */
	@DisplayName("Test UserController.getUser()")
	@Test
	public void getUserTest()
	{
		try {
			userController.getUser(123L);
		} catch (ApiException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Method to test UserController.updateUser method
	 */
	@DisplayName("Test UserController.updateUser()")
	@Test
	public void updateUserTest()
	{
		try {
			userController.updateUser(123L, new UpdateUserRequest());
		} catch (ApiException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Method to test UserController.searchUser method
	 */
	@DisplayName("Test UserController.searchUser()")
	@Test
	public void searchUserTest()
	{
		try {
			userController.searchUser(new SearchUserRequest());
		} catch (ApiException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Method to test UserController.verifyEmail method
	 */
	@DisplayName("Test UserController.verifyEmail()")
	@Test
	public void verifyEmailTest()
	{
		try {
			userController.verifyEmail(123L, "acode");
		} catch (ApiException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Method to test UserController.approve method
	 */
	@DisplayName("Test UserController.approve()")
	@Test
	public void approveTest()
	{
		try {
			userController.approve(123L);
		} catch (ApiException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Method to test UserController.deny method
	 */
	@DisplayName("Test UserController.deny()")
	@Test
	public void denyTest()
	{
		try {
			userController.deny(133L);
		} catch (ApiException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Method to test UserController.forgotPassword method
	 */
	@DisplayName("Test UserController.forgotPassword()")
	@Test
	public void forgotPasswordTest()
	{
		try {
			userController.forgotPassword(new ForgotPasswordRequest());
		} catch (ApiException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Method to test UserController.resetPassword method
	 */
	@DisplayName("Test UserController.resetPassword()")
	@Test
	public void resetPasswordTest()
	{
		try {
			userController.resetPassword(123L, new ResetPasswordRequest());
		} catch (ApiException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Method to test UserController.enable method
	 */
	@DisplayName("Test UserController.enable()")
	@Test
	public void enableTest()
	{
		try {
			userController.enable(123L);
		} catch (ApiException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Method to test UserController.disable method
	 */
	@DisplayName("Test UserController.disable()")
	@Test
	public void disableTest()
	{
		try {
			userController.disable(123L);
		} catch (ApiException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Method to test UserController.changePassword method
	 */
	@DisplayName("Test UserController.changePassword()")
	@Test
	public void changePasswordTest()
	{
		try {
			userController.changePassword(123L, new ChangePasswordRequest());
		} catch (ApiException e) {
			e.printStackTrace();
		}
	}
}
