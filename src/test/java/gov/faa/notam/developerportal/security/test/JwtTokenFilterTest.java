package gov.faa.notam.developerportal.security.test;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import gov.faa.notam.developerportal.security.JwtTokenFilter;
import gov.faa.notam.developerportal.security.JwtTokenProvider;

/**
 * Test class for JwtTokenFilter
 * @author AjithCK
 *
 */
@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
public class JwtTokenFilterTest 
{
	/**
	 * Class under test
	 */
	JwtTokenFilter jwtTokenFilter;
	
	/**
	 * Mocking JwtTokenProvider
	 */
	@Mock
	private JwtTokenProvider jwtTokenProvider;
	
	/**
	 * Initialize JwtTokenFilter
	 */
	@BeforeEach
	void setupEach() 
	{
		jwtTokenFilter = new JwtTokenFilter(jwtTokenProvider);
	}
	
	/**
	 * Method to testJwtTokenFilter.doFilter method.
	 */
	@DisplayName("Test JwtTokenFilter.doFilter()")
	@Test 
	public void doFilterTest() 
	{ 
		ServletRequest   servletRequest   = Mockito.mock(HttpServletRequest.class);
		ServletResponse  servletResponse  = Mockito.mock(ServletResponse.class);
		FilterChain      filterChain      = Mockito.mock(FilterChain.class);
		
		try 
		{
			Mockito.when(jwtTokenProvider.resolveToken((HttpServletRequest) servletRequest)).thenReturn("token");
			Mockito.when(jwtTokenProvider.validateToken("token")).thenReturn(true);
			Mockito.when(jwtTokenProvider.getAuthentication("token")).thenReturn(new UsernamePasswordAuthenticationToken("", ""));
			
			jwtTokenFilter.doFilter(servletRequest, servletResponse, filterChain);
		} catch (IOException e) 
		{
			e.printStackTrace();
		} catch (ServletException e) 
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Method to testJwtTokenFilter.doFilter method.
	 * Method to cover the branch coverage by making if condition false 
	 */
	@DisplayName("Test JwtTokenFilter.doFilter()")
	@Test 
	public void doFilter2Test() 
	{ 
		ServletRequest   servletRequest  = Mockito.mock(HttpServletRequest.class);
		ServletResponse  servletResponse = Mockito.mock(ServletResponse.class);
		FilterChain      filterChain     = Mockito.mock(FilterChain.class);
		try 
		{
			Mockito.when(jwtTokenProvider.resolveToken((HttpServletRequest) servletRequest)).thenReturn("token");
			Mockito.when(jwtTokenProvider.validateToken("token")).thenReturn(false);
			
			jwtTokenFilter.doFilter(servletRequest, servletResponse, filterChain);
		} catch (IOException e)
		{
			e.printStackTrace();
		} catch (ServletException e) 
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Method to testJwtTokenFilter.doFilter method.
	 * Method to cover branch coverage by making if condition false by returning token as null
	 */
	@DisplayName("Test JwtTokenFilter.doFilter() - Else check")
	@Test 
	public void doFilter1Test() 
	{ 
		ServletRequest   servletRequest  = Mockito.mock(HttpServletRequest.class);
		ServletResponse  servletResponse = Mockito.mock(ServletResponse.class);
		FilterChain      filterChain     = Mockito.mock(FilterChain.class);
		try 
		{
			Mockito.when(jwtTokenProvider.resolveToken((HttpServletRequest) servletRequest)).thenReturn(null);
			
			jwtTokenFilter.doFilter(servletRequest, servletResponse, filterChain);
		} catch (IOException e)
		{
			e.printStackTrace();
		} catch (ServletException e)
		{
			e.printStackTrace();
		}
	}
	
}
