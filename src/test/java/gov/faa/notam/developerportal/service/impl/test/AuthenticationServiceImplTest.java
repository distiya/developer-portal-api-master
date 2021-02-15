package gov.faa.notam.developerportal.service.impl.test;

import gov.faa.notam.developerportal.exception.ApiException;
import gov.faa.notam.developerportal.model.api.LoginRequest;
import gov.faa.notam.developerportal.model.api.LoginResponse;
import gov.faa.notam.developerportal.model.entity.User;
import gov.faa.notam.developerportal.repository.UserRepository;
import gov.faa.notam.developerportal.security.JwtTokenProvider;
import gov.faa.notam.developerportal.service.impl.AuthenticationServiceImpl;
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
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

/**
 * Test class for AuthenticationServiceImpl
 * @author AjithCK
 *
 */
@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
public class AuthenticationServiceImplTest
{
	private static final String AUTHENTICATION_FAILED =
            "Authentication failed, either the provided email does not exist, or the password is wrong.";

	/**
	 * Class under test
	 */
	AuthenticationServiceImpl authenticationServiceImpl;
	
	/**
	 * Mocking UserRepository
	 */
	@Mock
	private  UserRepository userRepository;

	/**
	 * Mocking PasswordEncoder
	 */
	@Mock
	private  PasswordEncoder passwordEncoder;

	/**
	 * Mocking JwtTokenProvider
	 */
	@Mock
	private  JwtTokenProvider jwtTokenProvider;
	
	/**
	 * Initialize AuthenticationServiceImpl
	 */
	@BeforeEach
    void setupEach()
	{
		authenticationServiceImpl = new AuthenticationServiceImpl(userRepository, passwordEncoder, jwtTokenProvider);
	}
	
	/**
	 * Test method for AuthenticationServiceImpl.login method.
	 */
	@DisplayName("Test AuthenticationServiceImpl.login()")
	@Test
	public void loginTest()
	{
		LoginRequest request = new LoginRequest();
		request.setEmail("aaa@aaa");
		request.setPassword("aaa@aaa");
		
		LoginResponse loginResponse = new LoginResponse("testtoken");
		
		User user = new User();
		user.setEnabled(true);
        user.setApproved(true);
        user.setApproved(true);
        user.setEmailConfirmed(true);
        user.setPasswordHash("aaa@aaa");
        
		try {
			Mockito.when(userRepository.findByEmail("aaa@aaa")).thenReturn(Optional.ofNullable(user));
			Mockito.when(passwordEncoder.matches("aaa@aaa", "aaa@aaa")).thenReturn(true);
			Mockito.when(jwtTokenProvider.generateToken(user)).thenReturn("testtoken");
			
			LoginResponse returnValue = authenticationServiceImpl.login(request);
			
			Assertions.assertEquals(returnValue, loginResponse);
		} catch (ApiException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Test method for AuthenticationServiceImpl.login method
	 * This method checks whether exception is triggering correctly when condition matches.
	 */
	@DisplayName("Test AuthenticationServiceImpl.login()")
	@Test
	public void loginErrorElseTest()
	{
		LoginRequest request = new LoginRequest();
		request.setEmail("aaa@aaa");
		request.setPassword("aaa@aaa");
		
		User user = new User();
		user.setEnabled(false);
        user.setApproved(true);
        user.setApproved(true);
        user.setEmailConfirmed(true);
        user.setPasswordHash("aaa@aaa");
        
        Mockito.when(userRepository.findByEmail("aaa@aaa")).thenReturn(Optional.ofNullable(user));
		
        ApiException exception = Assertions.assertThrows(ApiException.class, () -> {
			authenticationServiceImpl.login(request);
		});
        
        Assertions.assertTrue(exception.getMessage().contains(AUTHENTICATION_FAILED));
	}
	
	/**
	 * Method to test AuthenticationServiceImpl.login method.
	 * This method is to cover the branch coverage condition in exception thrown scenario.
	 */
	@DisplayName("Test AuthenticationServiceImpl.login()")
	@Test
	public void loginErrorElse2Test()
	{
		LoginRequest request = new LoginRequest();
		request.setEmail("aaa@aaa");
		request.setPassword("aaa@aaa");
		
		User user = new User();
		user.setEnabled(true);
        user.setApproved(null);
        user.setEmailConfirmed(true);
        user.setPasswordHash("aaa@aaa");

        Mockito.when(userRepository.findByEmail("aaa@aaa")).thenReturn(Optional.ofNullable(user));
		
        ApiException exception = Assertions.assertThrows(ApiException.class, () -> {
			authenticationServiceImpl.login(request);
		});
        
        Assertions.assertTrue(exception.getMessage().contains(AUTHENTICATION_FAILED));
	}
	
	/**
	 * Method to test AuthenticationServiceImpl.login method.
	 * This method is to cover the branch coverage condition in exception thrown scenario.
	 */
	@DisplayName("Test AuthenticationServiceImpl.login()")
	@Test
	public void loginErrorElse3Test()
	{
		LoginRequest request = new LoginRequest();
		request.setEmail("aaa@aaa");
		request.setPassword("aaa@aaa");
		
		User user = new User();
		user.setEnabled(true);
        user.setApproved(false);
        user.setEmailConfirmed(true);
        user.setPasswordHash("aaa@aaa");

        Mockito.when(userRepository.findByEmail("aaa@aaa")).thenReturn(Optional.ofNullable(user));
		
        ApiException exception = Assertions.assertThrows(ApiException.class, () -> {
			authenticationServiceImpl.login(request);
		});
        
        Assertions.assertTrue(exception.getMessage().contains(AUTHENTICATION_FAILED));
	}
	
	/**
	 * Method to test AuthenticationServiceImpl.login method.
	 * This method is to cover the branch coverage condition in exception thrown scenario.
	 */
	@DisplayName("Test AuthenticationServiceImpl.login()")
	@Test
	public void loginErrorElse4Test()
	{
		LoginRequest request = new LoginRequest();
		request.setEmail("aaa@aaa");
		request.setPassword("aaa@aaa");
		
		User user = new User();
		user.setEnabled(true);
        user.setApproved(true);
        user.setEmailConfirmed(false);
        user.setPasswordHash("aaa@aaa");

        Mockito.when(userRepository.findByEmail("aaa@aaa")).thenReturn(Optional.ofNullable(user));
		
        ApiException exception = Assertions.assertThrows(ApiException.class, () -> {
			authenticationServiceImpl.login(request);
		});
        
        Assertions.assertTrue(exception.getMessage().contains(AUTHENTICATION_FAILED));
	}
	
	/**
	 * Method to test AuthenticationServiceImpl.login method.
	 * This method is to cover the branch coverage condition in exception thrown scenario.
	 */
	@DisplayName("Test AuthenticationServiceImpl.login()")
	@Test
	public void loginErrorElse5Test()
	{
		LoginRequest request = new LoginRequest();
		request.setEmail("aaa@aaa");
		request.setPassword("aaa@aaa");
		
		User user = new User();
		user.setEnabled(true);
        user.setApproved(true);
        user.setEmailConfirmed(true);
        user.setPasswordHash("aaa@aaa");
			
        Mockito.when(userRepository.findByEmail("aaa@aaa")).thenReturn(Optional.ofNullable(user));
		Mockito.when(passwordEncoder.matches("aaa@aaa", "aaa@aaa")).thenReturn(false);
		
        ApiException exception = Assertions.assertThrows(ApiException.class, () -> {
			authenticationServiceImpl.login(request);
		});
        
        Assertions.assertTrue(exception.getMessage().contains(AUTHENTICATION_FAILED));
	}
	
	
	/**
	 * Method to test AuthenticationServiceImpl.login method.
	 * This method is to cover the branch coverage condition in exception thrown scenario.
	 */
	@DisplayName("Test AuthenticationServiceImpl.login() - Exception")
	@Test
	public void loginErrorTest()
	{
		LoginRequest request = new LoginRequest();
		request.setEmail("aaa@aaa");
		request.setPassword("aaa@aaa");
		
		Mockito.when(userRepository.findByEmail("aaa@aaa")).thenReturn(Optional.empty());
		
        ApiException exception = Assertions.assertThrows(ApiException.class, () -> {
			authenticationServiceImpl.login(request);
		});
        
        Assertions.assertTrue(exception.getMessage().contains(AUTHENTICATION_FAILED));
	}
}
