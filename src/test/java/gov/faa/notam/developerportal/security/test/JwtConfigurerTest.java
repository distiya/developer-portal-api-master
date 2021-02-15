package gov.faa.notam.developerportal.security.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import gov.faa.notam.developerportal.security.JwtConfigurer;
import gov.faa.notam.developerportal.security.JwtTokenProvider;

/**
 * Test call for JwtConfigurer
 * @author AjithCK
 *
 */
@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
public class JwtConfigurerTest
{

	/**
	 * Class under test
	 */
	JwtConfigurer jwtConfigurer;
	
	/**
	 * Mocking JwtTokenProvider
	 */
	@Mock
	private JwtTokenProvider jwtTokenProvider;
	
	/**
	 * Initialize JwtConfigurer.
	 */
	@BeforeEach
	void setupEach() 
	{
		jwtConfigurer = new JwtConfigurer(jwtTokenProvider);
	}
	
	/**
	 * Test method for JwtConfigurer.configure.
	 */
	@DisplayName("Test JwtConfigurer.configure()")
	@Test 
	public void doFilterTest() 
	{ 
		HttpSecurity builder = Mockito.mock(HttpSecurity.class);
		jwtConfigurer.configure(builder);
	}
}
