package gov.faa.notam.developerportal.security.test;

import java.time.Duration;
import java.time.Instant;

import javax.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;

import gov.faa.notam.developerportal.model.entity.User;
import gov.faa.notam.developerportal.model.entity.UserRole;
import gov.faa.notam.developerportal.security.JwtTokenProvider;

/**
 * Test class for JwtTokenProvider
 * @author AjithCK
 *
 */
@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
public class JwtTokenProviderTest 
{

	/**
	 * Class under test
	 */
	JwtTokenProvider jwtTokenProvider;
	
	/**
	 * Initialize JwtTokenProvider and set class variables .
	 */
	@BeforeEach
	void setupEach() 
	{
		Instant   start     = Instant.parse("2017-10-03T10:15:30.00Z");
		Instant   end       = Instant.parse("2017-10-03T10:16:30.00Z");
		Duration  duration  = Duration.between(start, end);
		
		jwtTokenProvider = new JwtTokenProvider();
		
		ReflectionTestUtils.setField(jwtTokenProvider, "jwtSecretBase64Encoded", "1234567812345678123456781234567812345678562323");
		ReflectionTestUtils.setField(jwtTokenProvider, "jwtValidDuration", duration);
		
	}
	
	/**
	 * Method to test JwtTokenProvider.validateToken method.
	 */
	@DisplayName("Test JwtTokenProvider.validateToken()")
	@Test 
	public void validateTokenTest() 
	{ 
		User user = new User();
		user.setRole(UserRole.ADMIN);
		user.setEmail("aaa@aaa");
		user.setId(123L);
		
		String token = jwtTokenProvider.generateToken(user);
		
		boolean retrunValue = jwtTokenProvider.validateToken(token);
		
		Assertions.assertEquals(retrunValue, true);

	}
	
	/**
	 * Method to test JwtTokenProvider.getAuthentication method.
	 */
	@DisplayName("Test JwtTokenProvider.getAuthentication()")
	@Test 
	public void getAuthenticationTest() 
	{ 
		User user = new User();
		user.setRole(UserRole.ADMIN);
		user.setEmail("aaa@aaa");
		user.setId(123L);
		
		String token = jwtTokenProvider.generateToken(user);
		
		Authentication authentication = jwtTokenProvider.getAuthentication(token);
		
		Assertions.assertNotNull(authentication);

	}
	
	/**
	 * Method to test JwtTokenProvider.resolveToken method.
	 */
	@DisplayName("Test JwtTokenProvider.resolveToken()")
	@Test 
	public void resolveTokenTest() 
	{ 
		HttpServletRequest request = Mockito.mock(HttpServletRequest .class);
		Mockito.when(request.getHeader("Authorization")).thenReturn("bearer ");
		
		String returnValue = jwtTokenProvider.resolveToken(request);
		
		Assertions.assertEquals(returnValue, "");
	}
	
	/**
	 * Method to test JwtTokenProvider.resolveToken method.
	 * This method skips the if condition and return null value
	 */
	@DisplayName("Test JwtTokenProvider.resolveToken()")
	@Test 
	public void resolveTokenElseTest() 
	{ 
		HttpServletRequest request = Mockito.mock(HttpServletRequest .class);
		Mockito.when(request.getHeader("Authorization")).thenReturn(null);
		
		String returnValue = jwtTokenProvider.resolveToken(request);
		
		Assertions.assertNull(returnValue);
	}
	
	/**
	 * Method to test JwtTokenProvider.resolveToken method.
	 * This method skips the if condition and return null value
	 */
	@DisplayName("Test JwtTokenProvider.resolveToken() - Return null")
	@Test 
	public void resolveTokenReturnNullTest() 
	{ 
		HttpServletRequest request = Mockito.mock(HttpServletRequest .class);
		Mockito.when(request.getHeader("Authorization")).thenReturn("asd ");
		
		String returnValue = jwtTokenProvider.resolveToken(request);
		
		Assertions.assertNull(returnValue);
	}
	
	/**
	 * Method to test JwtTokenProvider.validateToken method.
	 * This method is to check whether exception is thrown or not for the invalid request.
	 */
	@DisplayName("Test JwtTokenProvider.validateToken() Exception ")
	@Test 
	public void validateTokenExceptionTest() 
	{ 
		BadCredentialsException exception = Assertions.assertThrows(BadCredentialsException.class, () -> {
			jwtTokenProvider.validateToken("1234");
		});
		
		Assertions.assertTrue(exception.getMessage().contains("Expired or invalid JWT token"));
	}
}
