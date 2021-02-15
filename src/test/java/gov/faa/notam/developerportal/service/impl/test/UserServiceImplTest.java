package gov.faa.notam.developerportal.service.impl.test;

import gov.faa.notam.developerportal.configuration.PaginationConfig;
import gov.faa.notam.developerportal.exception.ApiException;
import gov.faa.notam.developerportal.model.api.*;
import gov.faa.notam.developerportal.model.entity.User;
import gov.faa.notam.developerportal.model.entity.UserRole;
import gov.faa.notam.developerportal.repository.UserRepository;
import gov.faa.notam.developerportal.security.PasswordPolicy;
import gov.faa.notam.developerportal.security.ReCaptcha;
import gov.faa.notam.developerportal.security.SecurityUtil;
import gov.faa.notam.developerportal.service.EmailService;
import gov.faa.notam.developerportal.service.impl.UserServiceImpl;
import gov.faa.notam.developerportal.service.impl.UserSpecification;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.Assertions;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Test class for UserServiceImpl
 * @author AjithCK
 *
 */
@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
public class UserServiceImplTest
{

	/**
	 * Mocking UserRepository
	 */
	@Mock
	private UserRepository userRepository;

	/**
	 * Mocking ReCaptcha
	 */
	@Mock
	private ReCaptcha reCaptcha;

	/**
	 * Mocking PasswordPolicy
	 */
	@Mock
	private PasswordPolicy passwordPolicy;

	/**
	 * Mocking PasswordEncoder
	 */
	@Mock
	private PasswordEncoder passwordEncoder;

	/**
	 * Mocking EmailService
	 */
	@Mock
	private EmailService emailService;

	/**
	 * Mocking PaginationConfig
	 */
	@Mock
	private PaginationConfig paginationConfig;

	/**
	 * Class under test
	 */
	UserServiceImpl userServiceImpl;

	/**
	 * Initialize UserServiceImpl
	 */
	@BeforeEach
	public void setup()
	{
	  userServiceImpl = new UserServiceImpl(userRepository, reCaptcha, passwordPolicy, passwordEncoder, emailService, paginationConfig);
	}



	/**
	 * Test method for UserServiceImpl.registerUser method.
	 */
	@DisplayName("Test UserServiceImpl.registerUser()")
	@Test
	public void registerUserTest()
	{
		RegisterUserRequest registerUserRequest = new RegisterUserRequest();
		registerUserRequest.setReCaptchaResponse("aaa");
		registerUserRequest.setEmail("test@test.com");
		registerUserRequest.setPassword("test123");
		try 
		{
			Mockito.when(reCaptcha.verifyResponse("aaa")).thenReturn(true);
			Mockito.when(passwordPolicy.validatePassword("test@test.com", "test123")).thenReturn(true);
			
			userServiceImpl.registerUser(registerUserRequest);
		} catch (ApiException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Test method for UserServiceImpl.registerUser method.
	 * This method tests already existing user scenario
	 */
	@DisplayName("Test UserServiceImpl.registerUser()")
	@Test
	public void registerUser1Test()
	{
		RegisterUserRequest registerUserRequest = new RegisterUserRequest();
		registerUserRequest.setReCaptchaResponse("aaa");
		registerUserRequest.setEmail("test@test.com");
		registerUserRequest.setPassword("test123");
		try {
			User user = new User();
			user.setDeleted(true);
			
			Mockito.when(userRepository.findByEmailIncludingDeleted("test@test.com")).thenReturn(Optional.ofNullable(user));
			Mockito.when(reCaptcha.verifyResponse("aaa")).thenReturn(true);
			Mockito.when(passwordPolicy.validatePassword("test@test.com", "test123")).thenReturn(true);
			Mockito.when(userRepository.save(user)).thenReturn(user);
			
			userServiceImpl.registerUser(registerUserRequest);
		} catch (ApiException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * This method test UserServiceImpl.registerUser() method.
	 * This is to check whether exception is thrown when already registered email is given.
	 */
	@DisplayName("Test UserServiceImpl.registerUser() Email already registered exception")
	@Test
	public void registerUser2Test() {
		RegisterUserRequest registerUserRequest = new RegisterUserRequest();
		registerUserRequest.setReCaptchaResponse("aaa");
		registerUserRequest.setEmail("test@test.com");
		registerUserRequest.setPassword("test123");

		User user = new User();
		user.setDeleted(false);
		user.setApproved(true);
		
		Mockito.when(userRepository.findByEmailIncludingDeleted("test@test.com")).thenReturn(Optional.ofNullable(user));
		Mockito.when(reCaptcha.verifyResponse("aaa")).thenReturn(true);
		
		ApiException exception = Assertions.assertThrows(ApiException.class, () -> {
			userServiceImpl.registerUser(registerUserRequest);
		});
		
		Assertions.assertTrue(exception.getMessage().contains("Email already registered"));
	}
	
	/**
	 * This method test UserServiceImpl.registerUser() method.
	 * This is to check whether exception is thrown when recaptcha fails.
	 */
	@DisplayName("Test UserServiceImpl.registerUser() - ReCaptcha failed.")
	@Test
	public void registerUser3Test() 
	{
		RegisterUserRequest registerUserRequest = new RegisterUserRequest();
		registerUserRequest.setReCaptchaResponse("aaa");
		registerUserRequest.setEmail("test@test.com");
		registerUserRequest.setPassword("test123");
	
		Mockito.when(reCaptcha.verifyResponse("aaa")).thenReturn(false);
		
		ApiException exception = Assertions.assertThrows(ApiException.class, () -> {
			userServiceImpl.registerUser(registerUserRequest);
		});
		
		Assertions.assertTrue(exception.getMessage().contains("ReCaptcha failed"));
	}
	

	/**
	 * Method to test UserServiceImpl.registerAdmin() test
	 */
	@DisplayName("Test UserServiceImpl.registerAdmin()")
	@Test
	public void registerAdminTest()
	{
		RegisterAdminRequest registerUserRequest = new RegisterAdminRequest();
		registerUserRequest.setEmail("test@test.com");
		registerUserRequest.setPassword("test123");
		try 
		{
			User user=new User();
			user.setDeleted(true);
			
			Mockito.when(passwordPolicy.validatePassword("test@test.com", "test123")).thenReturn(true);
			Mockito.when(userRepository.findByEmailIncludingDeleted(registerUserRequest.getEmail())).thenReturn(Optional.ofNullable(user));
			
			userServiceImpl.registerAdmin(registerUserRequest);
		} catch (ApiException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Method to test UserServiceImpl.registerAdmin() test
	 * This method is to check whether exception is thrown for already registered email.
	 */
	@DisplayName("Test UserServiceImpl.registerAdmin() -Email address already exists")
	@Test
	public void registerAdmin1Test()
	{
		RegisterAdminRequest registerUserRequest = new RegisterAdminRequest();
		registerUserRequest.setEmail("test@test.com");
		registerUserRequest.setPassword("test123");
		
		User user=new User();
		user.setDeleted(false);
		
		Mockito.when(userRepository.findByEmailIncludingDeleted(registerUserRequest.getEmail())).thenReturn(Optional.ofNullable(user));
		
		ApiException exception = Assertions.assertThrows(ApiException.class, ()->{
			userServiceImpl.registerAdmin(registerUserRequest);
		});
		
		Assertions.assertTrue(exception.getMessage().contains("Email address already exists."));
	}
	
	/**
	 * Method to test UserServiceImpl.registerAdmin() test
	 * This method is to check whether exception is thrown when could not create user
	 */
	@DisplayName("Test UserServiceImpl.registerAdmin()- Could not create user:")
	@Test
	public void registerAdmin2Test() 
	{
		RegisterAdminRequest registerUserRequest = new RegisterAdminRequest();
		registerUserRequest.setEmail("test@test.com");
		registerUserRequest.setPassword("test123");

		User user=new User();
		user.setDeleted(true);
		
		Mockito.when(passwordPolicy.validatePassword("test@test.com", "test123")).thenReturn(true);
		Mockito.when(userRepository.findByEmailIncludingDeleted(registerUserRequest.getEmail())).thenReturn(Optional.ofNullable(user));
		Mockito.when(userRepository.save(user)).thenThrow(new ConstraintViolationException("", new SQLException(), ""));

		ApiException exception = Assertions.assertThrows(ApiException.class, ()->{
			userServiceImpl.registerAdmin(registerUserRequest);
		});
		
		Assertions.assertTrue(exception.getMessage().contains("Could not create user"));
	}


	/**
	 * Method to test UserServiceImpl.forgotPassword method.
	 * This method is to check whether user not approved yet exception is working.
	 */
	@DisplayName("Test UserServiceImpl.forgotPassword()")
	@Test
	public void forgotPasswordTest()
	{
		ForgotPasswordRequest forgotPasswordRequest = new ForgotPasswordRequest();
		forgotPasswordRequest.setEmail("abc@abc.com");

		User user = new User();
		Mockito.when(userRepository.findByEmail("abc@abc.com")).thenReturn(Optional.ofNullable(user));
		
		ApiException exception = Assertions.assertThrows(ApiException.class, ()-> {
			userServiceImpl.forgotPassword(forgotPasswordRequest);
		});
		
		Assertions.assertTrue(exception.getMessage().contains("User not approved yet"));
	}
	
	/**
	 * Method to check UserServiceImpl.forgotPassword() method.
	 */
	@DisplayName("Test UserServiceImpl.forgotPassword()")
	@Test
	public void forgotPassword1Test()
	{
		ForgotPasswordRequest forgotPasswordRequest = new ForgotPasswordRequest();
		forgotPasswordRequest.setEmail("abc@abc.com");
		try {
			User user = new User();
			user.setApproved(true);
			
			Mockito.when(userRepository.findByEmail("abc@abc.com")).thenReturn(Optional.ofNullable(user));
			
			userServiceImpl.forgotPassword(forgotPasswordRequest);
		} catch (ApiException e) {
		}
	}
	
	/**
	 * Method to test UserServiceImpl.forgotPassword() method.
	 * This method do nothing when given email is not found.
	 */
	@DisplayName("Test UserServiceImpl.forgotPassword() do nothing if email not found")
	@Test
	public void forgotPassword2Test() 
	{
		ForgotPasswordRequest forgotPasswordRequest = new ForgotPasswordRequest();
		forgotPasswordRequest.setEmail("abc@abc.com");
		try {
			Mockito.when(userRepository.findByEmail("abc@abc.com")).thenReturn(Optional.ofNullable(null));
			
			userServiceImpl.forgotPassword(forgotPasswordRequest);
		} catch (ApiException e) {
		}
	}

	/**
	 * Method to test UserServiceImpl.changePassword() method
	 */
	@DisplayName("Test UserServiceImpl.changePassword()")
	@Test
	public void changePasswordTest() 
	{
		ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest();
		// Mock scope
		try (MockedStatic<gov.faa.notam.developerportal.security.SecurityUtil> mocked = Mockito
				.mockStatic(SecurityUtil.class)) {
			mocked.when(SecurityUtil::getCurrentUserId).thenReturn(Optional.of(123L));
			
			User retrieved = new User();
			changePasswordRequest.setCurrentPassword("123");
			retrieved.setPasswordHash("123");
			Mockito.when(userRepository.findById(123L)).thenReturn(Optional.ofNullable(retrieved));
			
			Mockito.when(passwordEncoder.matches("123", "123")).thenReturn(true);
			
			retrieved.setEmail("aaa@aaa");
			changePasswordRequest.setNewPassword("aaa@aaa");
			Mockito.when(passwordPolicy.validatePassword("aaa@aaa", "aaa@aaa")).thenReturn(true);
			
			try 
			{
				userServiceImpl.changePassword(123L, changePasswordRequest);
			} catch (ApiException e) {
			}
		}
	}
	
	/**
	 * Method to test UserServiceImpl.changePassword() method
	 * 
	 * Method to check whether exception is thrown when try change password of other user.
	 */
	@DisplayName("Test UserServiceImpl.changePassword() Can only change password of yourself")
	@Test
	public void changePassword1Test() 
	{
		ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest();
		// Mock scope
		try (MockedStatic<gov.faa.notam.developerportal.security.SecurityUtil> mocked = Mockito
				.mockStatic(SecurityUtil.class)) {
			mocked.when(SecurityUtil::getCurrentUserId).thenReturn(Optional.of(1231L));
			
			ApiException excetion = Assertions.assertThrows(ApiException.class, ()->{
				userServiceImpl.changePassword(123L, changePasswordRequest);
			});
			
			Assertions.assertTrue(excetion.getMessage().contains("Can only change password of yourself"));
		}
	}
	
	/**
	 * Method to test UserServiceImpl.changePassword() method
	 */
	@DisplayName("Test UserServiceImpl.changePassword()")
	@Test
	public void changePassword2Test() 
	{
		ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest();
		// Mock scope
		try (MockedStatic<gov.faa.notam.developerportal.security.SecurityUtil> mocked = Mockito
				.mockStatic(SecurityUtil.class)) {
			mocked.when(SecurityUtil::getCurrentUserId).thenReturn(Optional.of(123L));
			
			User retrieved = new User();
			changePasswordRequest.setCurrentPassword("123");
			retrieved.setPasswordHash("1233");
			Mockito.when(userRepository.findById(123L)).thenReturn(Optional.ofNullable(retrieved));
			
			Mockito.when(passwordEncoder.matches("123", "1233")).thenReturn(true);
			
			retrieved.setEmail("aaa@aaa");
			changePasswordRequest.setNewPassword("aaa@aaa");
			Mockito.when(passwordPolicy.validatePassword("aaa@aaa", "aaa@aaa")).thenReturn(true);
			
			try {

				userServiceImpl.changePassword(123L, changePasswordRequest);
			} catch (ApiException e) {
			}
		}
	}
	
	/**
	 * Method to test UserServiceImpl.changePassword() method
	 * Method to check whether exception is thrown when wrong current password is given. 
	 */
	@DisplayName("Test UserServiceImpl.changePassword()- Wrong password.")
	@Test
	public void changePassword3Test() 
	{
		ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest();
		// Mock scope
		try (MockedStatic<gov.faa.notam.developerportal.security.SecurityUtil> mocked = Mockito
				.mockStatic(SecurityUtil.class)) {
			mocked.when(SecurityUtil::getCurrentUserId).thenReturn(Optional.of(123L));
			
			User retrieved = new User();
			changePasswordRequest.setCurrentPassword("123");
			retrieved.setPasswordHash("123");
			Mockito.when(userRepository.findById(123L)).thenReturn(Optional.ofNullable(retrieved));
			
			Mockito.when(passwordEncoder.matches("123", "123")).thenReturn(false);
			
			retrieved.setEmail("aaa@aaa");
			changePasswordRequest.setNewPassword("aaa@aaa");
			
			ApiException excetion = Assertions.assertThrows(ApiException.class, ()->{
				userServiceImpl.changePassword(123L, changePasswordRequest);
			});
			
			Assertions.assertTrue(excetion.getMessage().contains("Wrong password"));
		}
	}
	
	/**
	 * Method to test UserServiceImpl.updateUser method 
	 */
	@DisplayName("Test UserServiceImpl.updateUser()")
	@Test
	public void updateUserTest() 
	{
		
		UpdateUserRequest request = new UpdateUserRequest();
		try (MockedStatic<gov.faa.notam.developerportal.security.SecurityUtil> mocked = Mockito
				.mockStatic(SecurityUtil.class))
		{
			User retrieved = new User();
			retrieved.setEmail("123");
			mocked.when(SecurityUtil::getCurrentUserId).thenReturn(Optional.of(123L));
			
			mocked.when(SecurityUtil::getCurrentUserRole).thenReturn(UserRole.ADMIN);
			
			Mockito.when(userRepository.findById(123L)).thenReturn(Optional.ofNullable(retrieved));
			Mockito.when(passwordPolicy.validatePassword("123", "123")).thenReturn(true);

			request.setPassword("123");
			request.setFullName("12");
			request.setCompany("qwe");
			request.setAddress("qwe");
			request.setCity("qwe");
			request.setState("qwe");
			request.setCountry("qw");
			request.setZipCode("qwe");
			request.setPrimaryPhone("qwe");
			request.setAlternatePhone("qwe");
			request.setNotamDataIntendedUsage("qwe");
			
			try {
				userServiceImpl.updateUser(123L, request);
			} catch (ApiException e) {
			}
		}
	}
	
	/**
	 * Method to test UserServiceImpl.updateUser method 
	 */
	@DisplayName("Test UserServiceImpl.updateUser()")
	@Test
	public void updateUserOppositTest() 
	{
		
		UpdateUserRequest request = new UpdateUserRequest();
		try (MockedStatic<gov.faa.notam.developerportal.security.SecurityUtil> mocked = Mockito
				.mockStatic(SecurityUtil.class))
		{
			User retrieved = new User();
			retrieved.setEmail("123");
			mocked.when(SecurityUtil::getCurrentUserId).thenReturn(Optional.of(123L));
			
			mocked.when(SecurityUtil::getCurrentUserRole).thenReturn(UserRole.ADMIN);
			
			Mockito.when(userRepository.findById(123L)).thenReturn(Optional.ofNullable(retrieved));
			
			try {
				userServiceImpl.updateUser(123L, request);
			} catch (ApiException e) {
			}
		}
	}
	
	/**
	 * Method to test UserServiceImpl.disableUser method 
	 */
	@DisplayName("Test UserServiceImpl.disableUser()")
	@Test
	public void disableUserTest() 
	{
		try (MockedStatic<gov.faa.notam.developerportal.security.SecurityUtil> mocked = Mockito
				.mockStatic(SecurityUtil.class))
		{
			User retrieved = new User();
			mocked.when(SecurityUtil::getCurrentUserId).thenReturn(Optional.of(1234L));
			
			Mockito.when(userRepository.findById(123L)).thenReturn(Optional.ofNullable(retrieved));
			
			try {
				userServiceImpl.disableUser(123L);
			} catch (ApiException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Method to test UserServiceImpl.disableUser method 
	 * 
	 * Method to check whether exception is thrown when try to disable same user.
	 */
	@DisplayName("Test UserServiceImpl.disableUser() Cannot disable yourself")
	@Test
	public void disableUser1Test() 
	{
		try (MockedStatic<gov.faa.notam.developerportal.security.SecurityUtil> mocked = Mockito
				.mockStatic(SecurityUtil.class))
		{
			mocked.when(SecurityUtil::getCurrentUserId).thenReturn(Optional.of(123L));
			
			ApiException exception =Assertions.assertThrows(ApiException.class, ()->{
				userServiceImpl.disableUser(123L);
			});
			
			Assertions.assertTrue(exception.getMessage().contains("Cannot disable yourself"));
		}
	}
	
	/**
	 * Method to test UserServiceImpl.enableUser method 
	 */
	@DisplayName("Test UserServiceImpl.enableUser()")
	@Test
	public void enableUserTest() 
	{
		try (MockedStatic<gov.faa.notam.developerportal.security.SecurityUtil> mocked = Mockito
				.mockStatic(SecurityUtil.class))
		{
			User retrieved = new User();
			mocked.when(SecurityUtil::getCurrentUserId).thenReturn(Optional.of(123L));
			
			Mockito.when(userRepository.findById(123L)).thenReturn(Optional.ofNullable(retrieved));
			
			try {
				userServiceImpl.enableUser(123L);
			} catch (ApiException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Method to test UserServiceImpl.enableUser method 
	 * 
	 * Methods to check whether exception is thrown when user is not found.
	 */
	@DisplayName("Test UserServiceImpl.enableUser() - Exception")
	@Test
	public void enableUser1Test() 
	{
		try (MockedStatic<gov.faa.notam.developerportal.security.SecurityUtil> mocked = Mockito
				.mockStatic(SecurityUtil.class))
		{
			mocked.when(SecurityUtil::getCurrentUserId).thenReturn(Optional.of(123L));
			
			Mockito.when(userRepository.findById(123L)).thenReturn(Optional.empty());
			
			ApiException exception =Assertions.assertThrows(ApiException.class, ()->{
				userServiceImpl.enableUser(123L);
			});
			
			Assertions.assertTrue(exception.getMessage().contains("User not found:"));
		}
	}
	
	/**
	 * Method to test UserServiceImpl.verifyEmail method 
	 */
	@DisplayName("Test UserServiceImpl.verifyEmail()")
	@Test
	public void verifyEmailTest() 
	{
		try (MockedStatic<gov.faa.notam.developerportal.security.SecurityUtil> mocked = Mockito
				.mockStatic(SecurityUtil.class))
		{
			User retrieved = new User();
			retrieved.setEmailConfirmationCode("123");
			mocked.when(SecurityUtil::getCurrentUserId).thenReturn(Optional.of(123L));
			
			Mockito.when(userRepository.findById(123L)).thenReturn(Optional.ofNullable(retrieved));
			
			try {
				userServiceImpl.verifyEmail(123L, "123");
			} catch (ApiException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Method to test UserServiceImpl.verifyEmail method .
	 * This method is to check whether exception is thrown correctly or not.
	 */
	@DisplayName("Test UserServiceImpl.verifyEmail()")
	@Test
	public void verifyEmail1Test() 
	{
		try (MockedStatic<gov.faa.notam.developerportal.security.SecurityUtil> mocked = Mockito
				.mockStatic(SecurityUtil.class))
		{
			User retrieved = new User();
			retrieved.setEmailConfirmationCode("123");
			mocked.when(SecurityUtil::getCurrentUserId).thenReturn(Optional.of(123L));
			
			Mockito.when(userRepository.findById(123L)).thenReturn(Optional.ofNullable(retrieved));
			
			ApiException exception =Assertions.assertThrows(ApiException.class, ()->{
				userServiceImpl.verifyEmail(123L, "1234");
			});
			
			Assertions.assertTrue(exception.getMessage().contains("User not found")); 
		}
	}
	
	/**
	 * Method to test UserServiceImpl.approveUser method 
	 */
	@DisplayName("Test UserServiceImpl.approveUser()")
	@Test
	public void approveUserTest() 
	{
		try (MockedStatic<gov.faa.notam.developerportal.security.SecurityUtil> mocked = Mockito
				.mockStatic(SecurityUtil.class))
		{
			User retrieved = new User();
			mocked.when(SecurityUtil::getCurrentUserId).thenReturn(Optional.of(123L));
			
			Mockito.when(userRepository.findById(123L)).thenReturn(Optional.ofNullable(retrieved));
			
			try 
			{
				userServiceImpl.approveUser(123L);
			} catch (ApiException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Method to test UserServiceImpl.approveUser method 
	 * 
	 * This method is to check whether exception is thrown correctly or not.
	 */
	@DisplayName("Test UserServiceImpl.approveUser() User already approved or denied")
	@Test
	public void approveUser1Test() 
	{
		try (MockedStatic<gov.faa.notam.developerportal.security.SecurityUtil> mocked = Mockito
				.mockStatic(SecurityUtil.class))
		{
			User retrieved = new User();
			retrieved.setApproved(true);
			mocked.when(SecurityUtil::getCurrentUserId).thenReturn(Optional.of(123L));
			
			Mockito.when(userRepository.findById(123L)).thenReturn(Optional.ofNullable(retrieved));
			
			ApiException exception =Assertions.assertThrows(ApiException.class, ()->{
				userServiceImpl.approveUser(123L);
			});
			
			Assertions.assertTrue(exception.getMessage().contains("User already approved or denied")); 
		}
	}
	
	
	/**
	 * Method to test UserServiceImpl.denyUser method 
	 */
	@DisplayName("Test UserServiceImpl.denyUser()")
	@Test
	public void denyUserTest() 
	{
		try (MockedStatic<gov.faa.notam.developerportal.security.SecurityUtil> mocked = Mockito
				.mockStatic(SecurityUtil.class))
		{
			User retrieved = new User();
			mocked.when(SecurityUtil::getCurrentUserId).thenReturn(Optional.of(123L));
			
			Mockito.when(userRepository.findById(123L)).thenReturn(Optional.ofNullable(retrieved));
			
			try {
				userServiceImpl.denyUser(123L);
			} catch (ApiException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Method to test UserServiceImpl.denyUser method 
	 * 
	 * This method is to check whether exception is thrown correctly or not.
	 */
	@DisplayName("Test UserServiceImpl.denyUser() User already approved or denied:")
	@Test
	public void denyUser1Test() 
	{
		try (MockedStatic<gov.faa.notam.developerportal.security.SecurityUtil> mocked = Mockito
				.mockStatic(SecurityUtil.class))
		{
			User retrieved = new User();
			retrieved.setApproved(true);
			mocked.when(SecurityUtil::getCurrentUserId).thenReturn(Optional.of(123L));
			
			Mockito.when(userRepository.findById(123L)).thenReturn(Optional.ofNullable(retrieved));
			
			ApiException exception =Assertions.assertThrows(ApiException.class, ()->{
				userServiceImpl.denyUser(123L);
			});
			
			Assertions.assertTrue(exception.getMessage().contains("User already approved or denied"));
		}
	}
	
	/**
	 * Method to test UserServiceImpl.deleteAdmin method 
	 */
	@DisplayName("Test UserServiceImpl.deleteAdmin()")
	@Test
	public void deleteAdminTest() 
	{
		try (MockedStatic<gov.faa.notam.developerportal.security.SecurityUtil> mocked = Mockito
				.mockStatic(SecurityUtil.class))
		{
			User retrieved = new User();
			mocked.when(SecurityUtil::getCurrentUserId).thenReturn(Optional.of(1234L));
			
			Mockito.when(userRepository.findById(123L)).thenReturn(Optional.ofNullable(retrieved));
			
			retrieved.setRole(UserRole.ADMIN);
			try {
				userServiceImpl.deleteAdmin(123L);
			} catch (ApiException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Method to test UserServiceImpl.deleteAdmin method 
	 * 
	 * This method is tocheck whether exception is thrown correctly or not.
	 */
	@DisplayName("Test UserServiceImpl.deleteAdmin() - You cannot delete yourself")
	@Test
	public void deleteAdmin1Test() 
	{
		try (MockedStatic<gov.faa.notam.developerportal.security.SecurityUtil> mocked = Mockito
				.mockStatic(SecurityUtil.class))
		{
			User retrieved = new User();
			mocked.when(SecurityUtil::getCurrentUserId).thenReturn(Optional.of(123L));
			
			
			retrieved.setRole(UserRole.ADMIN);
			
			ApiException exception =Assertions.assertThrows(ApiException.class, ()->{
				userServiceImpl.deleteAdmin(123L);
			});
			
			Assertions.assertTrue(exception.getMessage().contains("You cannot delete yourself"));
		}
	}
	
	/**
	 * Method to test UserServiceImpl.deleteAdmin method 
	 * 
	 * This method is to check whether exception is thrown correctly or not.
	 */
	@DisplayName("Test UserServiceImpl.deleteAdmin() You cannot delete regular user")
	@Test
	public void deleteAdmin2Test() 
	{
		try (MockedStatic<gov.faa.notam.developerportal.security.SecurityUtil> mocked = Mockito
				.mockStatic(SecurityUtil.class))
		{
			User retrieved = new User();
			mocked.when(SecurityUtil::getCurrentUserId).thenReturn(Optional.of(1234L));
			
			Mockito.when(userRepository.findById(123L)).thenReturn(Optional.ofNullable(retrieved));
			
			retrieved.setRole(UserRole.USER);
			
			ApiException exception =Assertions.assertThrows(ApiException.class, ()->{
				userServiceImpl.deleteAdmin(123L);
			});
			
			Assertions.assertTrue(exception.getMessage().contains("You cannot delete regular user"));
		}
	}
	
	
	/**
	 * Method to test UserServiceImpl.resetPassword method 
	 */
	@DisplayName("Test UserServiceImpl.resetPassword()")
	@Test
	public void resetPasswordTest() 
	{
		try (MockedStatic<gov.faa.notam.developerportal.security.SecurityUtil> mocked = Mockito
				.mockStatic(SecurityUtil.class))
		{
			User retrieved = new User();
			mocked.when(SecurityUtil::getCurrentUserId).thenReturn(Optional.of(123L));
			
			Mockito.when(userRepository.findById(123L)).thenReturn(Optional.ofNullable(retrieved));
			
			Mockito.when(passwordPolicy.validatePassword("111", "111")).thenReturn(true);
			
			ResetPasswordRequest request = new ResetPasswordRequest();
			request.setToken("111");
			retrieved.setPasswordResetToken("111");
			retrieved.setEmail("111");
			request.setNewPassword("111");
			
			try 
			{
				userServiceImpl.resetPassword(123L, request);
			} catch (ApiException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Method to test UserServiceImpl.resetPassword method 
	 * 
	 * This method is to check whether exception is thrown correctly or not.
	 */
	@DisplayName("Test UserServiceImpl.resetPassword()- Password validation failed")
	@Test
	public void resetPasswordExceptionTest() 
	{
		try (MockedStatic<gov.faa.notam.developerportal.security.SecurityUtil> mocked = Mockito
				.mockStatic(SecurityUtil.class))
		{
			User retrieved = new User();
			mocked.when(SecurityUtil::getCurrentUserId).thenReturn(Optional.of(123L));
			
			Mockito.when(userRepository.findById(123L)).thenReturn(Optional.ofNullable(retrieved));
			
			Mockito.when(passwordPolicy.validatePassword("111", "111")).thenReturn(false);
			
			ResetPasswordRequest request = new ResetPasswordRequest();
			request.setToken("111");
			retrieved.setPasswordResetToken("111");
			retrieved.setEmail("111");
			request.setNewPassword("111");
			
			ApiException exception =Assertions.assertThrows(ApiException.class, ()->{
				userServiceImpl.resetPassword(123L, request);
			});
			
			Assertions.assertTrue(exception.getMessage().contains("Password validation failed"));
		}
	}
	
	/**
	 * Method to test UserServiceImpl.resetPassword method 
	 * 
	 * This method is to check whether exception is thrown correctly or not.
	 */
	@DisplayName("Test UserServiceImpl.resetPassword() User not found.")
	@Test
	public void resetPassword1Test() 
	{
		try (MockedStatic<gov.faa.notam.developerportal.security.SecurityUtil> mocked = Mockito
				.mockStatic(SecurityUtil.class))
		{
			User retrieved = new User();
			mocked.when(SecurityUtil::getCurrentUserId).thenReturn(Optional.of(123L));
			
			Mockito.when(userRepository.findById(123L)).thenReturn(Optional.ofNullable(retrieved));
			
			
			ResetPasswordRequest request = new ResetPasswordRequest();
			request.setToken("111");
			retrieved.setPasswordResetToken("1111");
			retrieved.setEmail("111");
			request.setNewPassword("111");
			
			ApiException exception =Assertions.assertThrows(ApiException.class, ()->{
				userServiceImpl.resetPassword(123L, request);
			});
			
			Assertions.assertTrue(exception.getMessage().contains("User not found"));
		}
	}
	
	/**
	 * Method to test UserServiceImpl.searchUser method 
	 */
	@DisplayName("Test UserServiceImpl.searchUser()")
	@Test
	public void searchUserTest() 
	{
		SearchUserRequest         request  = new SearchUserRequest();
		try {
			Mockito.when(paginationConfig.toPageRequest(request)).
			thenReturn(PageRequest.of(3, 10, SortOrder.Asc.dir(), "aaa"));
			
			User       user     = new User();
			List<User> userList = new ArrayList<>();
			userList.add(user);
			Page<User> page = new PageImpl<>(userList);
			
			Mockito.when(userRepository.findAll(Mockito.any(UserSpecification.class), 
					Mockito.any(PageRequest.class))).thenReturn(page);
			
			SearchResponse<UserModel> retrunValue = userServiceImpl.searchUser(request);
			
			Assertions.assertNotNull(retrunValue);
		} catch (ApiException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Method to test UserServiceImpl.searchUser method 
	 * 
	 * This method is to check whether exception is thrown correctly or not.
	 */
	@DisplayName("Test UserServiceImpl.searchUser() - Failed to search user")
	@Test
	public void searchUser1Test() 
	{
		SearchUserRequest request  = new SearchUserRequest();
		User              user     = new User();
		List<User>        userList = new ArrayList<>();
		userList.add(user);
		
		Mockito.when(paginationConfig.toPageRequest(request)).
		thenReturn(PageRequest.of(3, 10, SortOrder.Asc.dir(), "aaa"));
		
		Mockito.when(userRepository.findAll(Mockito.any(UserSpecification.class), 
				Mockito.any(PageRequest.class))).thenReturn(null);
		
		
		ApiException exception =Assertions.assertThrows(ApiException.class, ()->{
			userServiceImpl.searchUser(request);
		});
		
		Assertions.assertTrue(exception.getMessage().contains("Failed to search user"));
	}
	
	/**
	 * Method to test UserServiceImpl.getUser method 
	 */
	@DisplayName("Test UserServiceImpl.getUser()")
	@Test
	public void getUserTest() 
	{
		try (MockedStatic<gov.faa.notam.developerportal.security.SecurityUtil> mocked = Mockito
				.mockStatic(SecurityUtil.class))
		{
			mocked.when(SecurityUtil::getCurrentUserId).thenReturn(Optional.of(123L));
			mocked.when(SecurityUtil::getCurrentUserRole).thenReturn(UserRole.ADMIN);
		
			User retrieved = new User();
			Mockito.when(userRepository.findById(123L)).thenReturn(Optional.ofNullable(retrieved));
			
			UserModel userModel = new UserModel(retrieved);
			try {
				UserModel returnValue = userServiceImpl.getUser(123L);
				Assertions.assertEquals(returnValue, userModel);
			} catch (ApiException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Method to test UserServiceImpl.getUser method 
	 * Method to check user has permission to get user.Throws exception 
	 */
	@DisplayName("Test UserServiceImpl.getUser() - Exception")
	@Test
	public void getUser1Test() 
	{
		try (MockedStatic<gov.faa.notam.developerportal.security.SecurityUtil> mocked = Mockito
				.mockStatic(SecurityUtil.class))
		{
			mocked.when(SecurityUtil::getCurrentUserId).thenReturn(Optional.of(1233L));
			mocked.when(SecurityUtil::getCurrentUserRole).thenReturn(UserRole.USER);
		
			ApiException exception = Assertions.assertThrows(ApiException.class, ()->{
				userServiceImpl.getUser(123L);
			});
			
			Assertions.assertTrue(exception.getMessage().contains("Regular user can access self information"));
		}
	}
}
