package gov.faa.notam.developerportal.service.impl.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import gov.faa.notam.developerportal.configuration.PaginationConfig;
import gov.faa.notam.developerportal.exception.ApiException;
import gov.faa.notam.developerportal.model.api.CreateNotamApiTokenRequest;
import gov.faa.notam.developerportal.model.api.NotamApiTokenModel;
import gov.faa.notam.developerportal.model.api.SearchNotamApiTokenRequest;
import gov.faa.notam.developerportal.model.api.SearchResponse;
import gov.faa.notam.developerportal.model.api.SortOrder;
import gov.faa.notam.developerportal.model.api.UpdateNotamApiTokenRequest;
import gov.faa.notam.developerportal.model.entity.NotamApiToken;
import gov.faa.notam.developerportal.model.entity.User;
import gov.faa.notam.developerportal.model.entity.UserRole;
import gov.faa.notam.developerportal.repository.NotamApiTokenRepository;
import gov.faa.notam.developerportal.repository.UserRepository;
import gov.faa.notam.developerportal.security.SecurityUtil;
import gov.faa.notam.developerportal.service.impl.NotamApiTokenServiceImpl;
import gov.faa.notam.developerportal.service.impl.NotamApiTokenSpecification;

/**
 * Test class for NotamApiTokenServiceImpl
 * @author AjithCK
 *
 */
@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
public class NotamApiTokenServiceImplTest 
{

	/**
	 * Class under test
	 */
	NotamApiTokenServiceImpl notamApiTokenServiceImpl;
	
	/**
	 * Mocking NotamApiTokenRepository
	 */
	@Mock
	private NotamApiTokenRepository notamApiTokenRepository;

	/**
	 * Mocking UserRepository
	 */
	@Mock
    private UserRepository userRepository;

	/**
	 * Mocking PaginationConfig
	 */
	@Mock
    private PaginationConfig paginationConfig;
	
	/**
	 * Initialize NotamApiTokenServiceImpl
	 */
	@BeforeEach
    void setupEach()
	{
		notamApiTokenServiceImpl = new NotamApiTokenServiceImpl(notamApiTokenRepository, userRepository, paginationConfig);
	}
	
	/**
	 * Method to test NotamApiTokenServiceImpl.createToken() method.
	 */
	@DisplayName("Test NotamApiTokenServiceImpl.createToken()")
	@Test
	public void createTokenTest()
	{
		try (MockedStatic<gov.faa.notam.developerportal.security.SecurityUtil> mocked = Mockito
				.mockStatic(SecurityUtil.class))
		{
			User retrieved = new User();
			retrieved.setEmail("123");
			mocked.when(SecurityUtil::getCurrentUserId).thenReturn(Optional.of(123L));
			
			Mockito.when(userRepository.findById(123L)).thenReturn(Optional.ofNullable(retrieved));
			NotamApiToken token = new NotamApiToken();
			Mockito.when(notamApiTokenRepository.save(Mockito.any(NotamApiToken.class))).thenReturn(token);
			
			NotamApiTokenModel notamApiTokenModel = new NotamApiTokenModel(token);
			
			CreateNotamApiTokenRequest createNotamApiTokenRequest = new CreateNotamApiTokenRequest();
			try {
				NotamApiTokenModel returnValue = notamApiTokenServiceImpl.createToken(createNotamApiTokenRequest);
				
				Assertions.assertEquals(returnValue, notamApiTokenModel);
			} catch (ApiException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Method to test NotamApiTokenServiceImpl.createToken() method.
	 * This method checks whether current user id not found exception is thrown or not.
	 */
	@DisplayName("Test NotamApiTokenServiceImpl.createToken() - Current user id not found.")
	@Test
	public void createToken1Test()
	{
		try (MockedStatic<gov.faa.notam.developerportal.security.SecurityUtil> mocked = Mockito
				.mockStatic(SecurityUtil.class))
		{
			mocked.when(SecurityUtil::getCurrentUserId).thenReturn(Optional.empty());
			
			CreateNotamApiTokenRequest createNotamApiTokenRequest = new CreateNotamApiTokenRequest();

			 ApiException exception = Assertions.assertThrows(ApiException.class, ()->{
				notamApiTokenServiceImpl.createToken(createNotamApiTokenRequest);
			});
			
			Assertions.assertTrue(exception.getMessage().contains("Current user id not found"));
		}
	}
	
	/**
	 * Method to test NotamApiTokenServiceImpl.createToken() method.
	 * This method checks whether Current user not found exception is thrown or not.
	 */
	@DisplayName("Test NotamApiTokenServiceImpl.createToken() Current user not found.")
	@Test
	public void createToken2Test()
	{
		try (MockedStatic<gov.faa.notam.developerportal.security.SecurityUtil> mocked = Mockito
				.mockStatic(SecurityUtil.class))
		{
			mocked.when(SecurityUtil::getCurrentUserId).thenReturn(Optional.of(123L));
			
			Mockito.when(userRepository.findById(123L)).thenReturn(Optional.empty());
			CreateNotamApiTokenRequest createNotamApiTokenRequest = new CreateNotamApiTokenRequest();

			ApiException exception = Assertions.assertThrows(ApiException.class, ()->{
				notamApiTokenServiceImpl.createToken(createNotamApiTokenRequest);
			});
			
			Assertions.assertTrue(exception.getMessage().contains("Current user not found"));
		}
	}
	
	/**
	 * Method to test NotamApiTokenServiceImpl.enable() method.
	 */
	@DisplayName("Test NotamApiTokenServiceImpl.enable()")
	@Test
	public void enableTest()
	{
		try (MockedStatic<gov.faa.notam.developerportal.security.SecurityUtil> mocked = Mockito
				.mockStatic(SecurityUtil.class))
		{
			NotamApiToken notamApiToken = new NotamApiToken();
			User user = new User();
			user.setId(123L);
			notamApiToken.setUser(user);
			mocked.when(SecurityUtil::getCurrentUserId).thenReturn(Optional.of(123L));
			mocked.when(SecurityUtil::getCurrentUserRole).thenAnswer(new Answer<UserRole>() 
			{
				private int count = 0;

			    public UserRole answer(InvocationOnMock invocation) {
			        if (count++ == 1)
			            return UserRole.USER;

			        return UserRole.ADMIN;
			    }
			});
			
			Mockito.when(notamApiTokenRepository.findById(123L)).thenReturn(Optional.ofNullable(notamApiToken));
			
			try {
				notamApiTokenServiceImpl.enable(123L);
			} catch (ApiException e) {
				e.printStackTrace();
			}

		}
	}
	
	/**
	 * Method to test NotamApiTokenServiceImpl.enable() method.
	 */
	@DisplayName("Test NotamApiTokenServiceImpl.enable() - ADMIN case")
	@Test
	public void enableADMINTest()
	{
		try (MockedStatic<gov.faa.notam.developerportal.security.SecurityUtil> mocked = Mockito
				.mockStatic(SecurityUtil.class))
		{
			NotamApiToken notamApiToken = new NotamApiToken();
			User user = new User();
			user.setId(123L);
			notamApiToken.setUser(user);
			mocked.when(SecurityUtil::getCurrentUserId).thenReturn(Optional.of(123L));
			mocked.when(SecurityUtil::getCurrentUserRole).thenReturn(UserRole.ADMIN);
			
			Mockito.when(notamApiTokenRepository.findById(123L)).thenReturn(Optional.ofNullable(notamApiToken));
			
			try {
				notamApiTokenServiceImpl.enable(123L);
			} catch (ApiException e) {
				e.printStackTrace();
			}

		}
	}
	
	/**
	 * Method to test NotamApiTokenServiceImpl.enable() method.
	 */
	@DisplayName("Test NotamApiTokenServiceImpl.enable() - ADMIN case")
	@Test
	public void enableADMINTElseTest()
	{
		try (MockedStatic<gov.faa.notam.developerportal.security.SecurityUtil> mocked = Mockito
				.mockStatic(SecurityUtil.class))
		{
			NotamApiToken notamApiToken = new NotamApiToken();
			User user = new User();
			user.setId(123L);
			notamApiToken.setUser(user);
			mocked.when(SecurityUtil::getCurrentUserId).thenAnswer(new Answer<Optional<Long>>() 
			{
				private int count = 0;

			    public Optional<Long> answer(InvocationOnMock invocation) {
			        if (count++ == 1)
			            return Optional.of(1243L);

			        return Optional.of(123L);
			    }
			});
			mocked.when(SecurityUtil::getCurrentUserRole).thenReturn(UserRole.ADMIN);
			
			Mockito.when(notamApiTokenRepository.findById(123L)).thenReturn(Optional.ofNullable(notamApiToken));
			
			try {
				notamApiTokenServiceImpl.enable(123L);
			} catch (ApiException e) {
				e.printStackTrace();
			}

		}
	}
	
	/**
	 * Method to test NotamApiTokenServiceImpl.disable() method.
	 */
	@DisplayName("Test NotamApiTokenServiceImpl.disable()")
	@Test
	public void disableTest()
	{
		try (MockedStatic<gov.faa.notam.developerportal.security.SecurityUtil> mocked = Mockito
				.mockStatic(SecurityUtil.class))
		{
			NotamApiToken notamApiToken = new NotamApiToken();
			User          user          = new User();
			
			user.setId(123L);
			notamApiToken.setUser(user);
			mocked.when(SecurityUtil::getCurrentUserId).thenReturn(Optional.of(123L));
			mocked.when(SecurityUtil::getCurrentUserRole).thenReturn(UserRole.ADMIN);
			Mockito.when(notamApiTokenRepository.findById(123L)).thenReturn(Optional.ofNullable(notamApiToken));
			
			try 
			{
				notamApiTokenServiceImpl.disable(123L);
			} catch (ApiException e) {
				e.printStackTrace();
			}

		}
	}
	
	/**
	 * Method to test NotamApiTokenServiceImpl.disable() method.
	 */
	@DisplayName("Test NotamApiTokenServiceImpl.disable()")
	@Test
	public void disableElseTest()
	{
		try (MockedStatic<gov.faa.notam.developerportal.security.SecurityUtil> mocked = Mockito
				.mockStatic(SecurityUtil.class))
		{
			NotamApiToken notamApiToken = new NotamApiToken();
			User          user          = new User();
			user.setId(123L);
			notamApiToken.setUser(user);
			mocked.when(SecurityUtil::getCurrentUserId).thenReturn(Optional.of(123L));
			mocked.when(SecurityUtil::getCurrentUserRole).thenReturn(UserRole.ADMIN);
			
			mocked.when(SecurityUtil::getCurrentUserId).thenAnswer(new Answer<Optional<Long>>() 
			{
				private int count = 0;

			    public Optional<Long> answer(InvocationOnMock invocation) {
			        if (count++ == 1)
			            return Optional.empty();

			        return Optional.of(123L);
			    }
			});
			
			Mockito.when(notamApiTokenRepository.findById(123L)).thenReturn(Optional.ofNullable(notamApiToken));
			
			try {
				notamApiTokenServiceImpl.disable(123L);
			} catch (ApiException e) {
				e.printStackTrace();
			}

		}
	}
	
	/**
	 * Method to test NotamApiTokenServiceImpl.disable() method.
	 * 
	 * This is to check whether exception is thrown correctly or not.
	 */
	@DisplayName("Test NotamApiTokenServiceImpl.disable()")
	@Test
	public void disableSwitchTest()
	{
		try (MockedStatic<gov.faa.notam.developerportal.security.SecurityUtil> mocked = Mockito
				.mockStatic(SecurityUtil.class))
		{
			NotamApiToken notamApiToken = new NotamApiToken();
			User user = new User();
			user.setId(1234L);
			notamApiToken.setUser(user);
			mocked.when(SecurityUtil::getCurrentUserId).thenReturn(Optional.of(123L));
			mocked.when(SecurityUtil::getCurrentUserRole).thenReturn(UserRole.ADMIN);
			Mockito.when(notamApiTokenRepository.findById(123L)).thenReturn(Optional.ofNullable(notamApiToken));
			
			ApiException exception = Assertions.assertThrows(ApiException.class, ()->{
				notamApiTokenServiceImpl.disable(123L);
			});
			
			Assertions.assertTrue(exception.getMessage().contains("Regular user can only access their own tokens"));
		}
	}
	
	/**
	 * Method to test NotamApiTokenServiceImpl.disable() method.
	 * 
	 * This is to check whether exception is thrown correctly or not
	 */
	@DisplayName("Test NotamApiTokenServiceImpl.disable() -  Exception")
	@Test
	public void disableExceptionTest()
	{
		try (MockedStatic<gov.faa.notam.developerportal.security.SecurityUtil> mocked = Mockito
				.mockStatic(SecurityUtil.class))
		{
			NotamApiToken notamApiToken = new NotamApiToken();
			User user = new User();
			user.setId(123L);
			notamApiToken.setUser(user);
			mocked.when(SecurityUtil::getCurrentUserId).thenReturn(Optional.empty());
			Mockito.when(notamApiTokenRepository.findById(123L)).thenReturn(Optional.ofNullable(notamApiToken));
			
			ApiException exception = Assertions.assertThrows(ApiException.class, ()->{
				notamApiTokenServiceImpl.disable(123L);
			});
			
			Assertions.assertTrue(exception.getMessage().contains("Current user id not found"));
		}
	}
	
	/**
	 * Method to test NotamApiTokenServiceImpl.disable() method.
	 */
	@DisplayName("Test NotamApiTokenServiceImpl.disable() - USER")
	@Test
	public void disableUSERTest()
	{
		try (MockedStatic<gov.faa.notam.developerportal.security.SecurityUtil> mocked = Mockito
				.mockStatic(SecurityUtil.class))
		{
			NotamApiToken notamApiToken = new NotamApiToken();
			User user = new User();
			user.setId(123L);
			notamApiToken.setUser(user);
			mocked.when(SecurityUtil::getCurrentUserId).thenReturn(Optional.of(123L));
			mocked.when(SecurityUtil::getCurrentUserRole).thenAnswer(new Answer<UserRole>() 
			{
				private int count = 0;

			    public UserRole answer(InvocationOnMock invocation) {
			        if (count++ == 1)
			            return UserRole.USER;

			        return UserRole.ADMIN;
			    }
			});
			Mockito.when(notamApiTokenRepository.findById(123L)).thenReturn(Optional.ofNullable(notamApiToken));
			
			try {
				notamApiTokenServiceImpl.disable(123L);
			} catch (ApiException e) {
				e.printStackTrace();
			}

		}
	}
	
	
	/**
	 * Method to test NotamApiTokenServiceImpl.updateToken() method.
	 */
	@DisplayName("Test NotamApiTokenServiceImpl.updateToken()")
	@Test
	public void updateTokenTest()
	{
		UpdateNotamApiTokenRequest request = new UpdateNotamApiTokenRequest();
		try (MockedStatic<gov.faa.notam.developerportal.security.SecurityUtil> mocked = Mockito
				.mockStatic(SecurityUtil.class))
		{
			NotamApiToken notamApiToken = new NotamApiToken();
			User          user = new User();
			
			user.setId(123L);
			notamApiToken.setUser(user);
			mocked.when(SecurityUtil::getCurrentUserId).thenReturn(Optional.of(123L));
			mocked.when(SecurityUtil::getCurrentUserRole).thenReturn(UserRole.ADMIN);
			Mockito.when(notamApiTokenRepository.findById(123L)).thenReturn(Optional.ofNullable(notamApiToken));
			
			try {
				notamApiTokenServiceImpl.updateToken(123L, request);
			} catch (ApiException e) {
				e.printStackTrace();
			}

		}
	}
	
	/**
	 * Method to test NotamApiTokenServiceImpl.deleteToken() method.
	 */
	@DisplayName("Test NotamApiTokenServiceImpl.deleteToken()")
	@Test
	public void deleteTokenTest()
	{
		try (MockedStatic<gov.faa.notam.developerportal.security.SecurityUtil> mocked = Mockito
				.mockStatic(SecurityUtil.class))
		{
			NotamApiToken notamApiToken = new NotamApiToken();
			User          user          = new User();
			
			user.setId(123L);
			notamApiToken.setUser(user);
			mocked.when(SecurityUtil::getCurrentUserId).thenReturn(Optional.of(123L));
			mocked.when(SecurityUtil::getCurrentUserRole).thenReturn(UserRole.ADMIN);
			
			Mockito.when(notamApiTokenRepository.findById(123L)).thenReturn(Optional.ofNullable(notamApiToken));
			
			try {
				notamApiTokenServiceImpl.deleteToken(123L);
			} catch (ApiException e) {
				e.printStackTrace();
			}

		}
	}
	
	/**
	 * Method to test NotamApiTokenServiceImpl.deleteToken() method.
	 * 
	 * Method to check whether exception is thrown correctly or not.
	 */
	@DisplayName("Test NotamApiTokenServiceImpl.deleteToken() Token not found.")
	@Test
	public void deleteToken1Test()
	{
		Mockito.when(notamApiTokenRepository.findById(123L)).thenReturn(Optional.ofNullable(null));
		
		ApiException exception = Assertions.assertThrows(ApiException.class, ()->{
			notamApiTokenServiceImpl.deleteToken(123L);
		});
		
		Assertions.assertTrue(exception.getMessage().contains("Token not found"));
	}
	
	/**
	 * Method to test NotamApiTokenServiceImpl.deleteToken() method.
	 * 
	 * Method to check whether exception is thrown correctly or not.
	 */
	@DisplayName("Test NotamApiTokenServiceImpl.deleteToken() Current user id not found")
	@Test
	public void deleteToken2Test()
	{
		try (MockedStatic<gov.faa.notam.developerportal.security.SecurityUtil> mocked = Mockito
				.mockStatic(SecurityUtil.class))
		{
			NotamApiToken notamApiToken = new NotamApiToken();
			User user = new User();
			user.setId(123L);
			notamApiToken.setUser(user);
			mocked.when(SecurityUtil::getCurrentUserId).thenReturn(Optional.of(123L));
			mocked.when(SecurityUtil::getCurrentUserRole).thenReturn(UserRole.USER);
			Mockito.when(notamApiTokenRepository.findById(123L)).thenReturn(Optional.ofNullable(notamApiToken));
			
			Assertions.assertThrows(ApiException.class, ()->{
				notamApiTokenServiceImpl.deleteToken(123L);
			});
		}
	}
	
	/**
	 * Method to test NotamApiTokenServiceImpl.getToken() method.
	 */
	@DisplayName("Test NotamApiTokenServiceImpl.getToken()")
	@Test
	public void getTokenTest()
	{
		try (MockedStatic<gov.faa.notam.developerportal.security.SecurityUtil> mocked = Mockito
				.mockStatic(SecurityUtil.class))
		{
			NotamApiToken notamApiToken = new NotamApiToken();
			User          user          = new User();
			
			user.setId(123L);
			notamApiToken.setUser(user);
			mocked.when(SecurityUtil::getCurrentUserId).thenReturn(Optional.of(123L));
			mocked.when(SecurityUtil::getCurrentUserRole).thenReturn(UserRole.ADMIN);
			
			Mockito.when(notamApiTokenRepository.findById(123L)).thenReturn(Optional.ofNullable(notamApiToken));
			NotamApiTokenModel tokenModel = new NotamApiTokenModel(notamApiToken);
			
			try {
				NotamApiTokenModel response = notamApiTokenServiceImpl.getToken(123L);
				
				Assertions.assertEquals(response, tokenModel);
			} catch (ApiException e) {
				e.printStackTrace();
			}

		}
	}
	
	/**
	 * Method to test NotamApiTokenServiceImpl.searchToken() method.
	 */
	@DisplayName("Test NotamApiTokenServiceImpl.searchToken()")
	@Test
	public void searchTokenTest()
	{
		SearchNotamApiTokenRequest request = new SearchNotamApiTokenRequest();
		try (MockedStatic<gov.faa.notam.developerportal.security.SecurityUtil> mocked = Mockito
				.mockStatic(SecurityUtil.class))
		{
			mocked.when(SecurityUtil::getCurrentUserId).thenReturn(Optional.of(123L));
			mocked.when(SecurityUtil::getCurrentUserRole).thenReturn(UserRole.USER);
			
			NotamApiToken notamApiToken = new NotamApiToken();
			List<NotamApiToken> notamApiTokenList = new ArrayList<>();
			notamApiTokenList.add(notamApiToken);
			Page<NotamApiToken> page = new PageImpl<>(notamApiTokenList);
			
			Mockito.when(paginationConfig.toPageRequest(request)).
			thenReturn(PageRequest.of(3, 10, SortOrder.Asc.dir(), "aaa"));
			
			Mockito.when(notamApiTokenRepository.findAll(Mockito.any(NotamApiTokenSpecification.class), 
					Mockito.any(PageRequest.class))).thenReturn(page);
			
			try {
				SearchResponse<NotamApiTokenModel> response = notamApiTokenServiceImpl.searchToken(request);
				
				Assertions.assertNotNull(response);
			} catch (ApiException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Method to test NotamApiTokenServiceImpl.searchToken() method.
	 */
	@DisplayName("Test NotamApiTokenServiceImpl.searchToken()")
	@Test
	public void searchTokenElseTest()
	{
		SearchNotamApiTokenRequest request = new SearchNotamApiTokenRequest();
		try (MockedStatic<gov.faa.notam.developerportal.security.SecurityUtil> mocked = Mockito
				.mockStatic(SecurityUtil.class))
		{
			mocked.when(SecurityUtil::getCurrentUserId).thenReturn(Optional.of(123L));
			mocked.when(SecurityUtil::getCurrentUserRole).thenReturn(UserRole.ADMIN);
			
			NotamApiToken notamApiToken = new NotamApiToken();
			List<NotamApiToken> notamApiTokenList = new ArrayList<>();
			notamApiTokenList.add(notamApiToken);
			Page<NotamApiToken> page = new PageImpl<>(notamApiTokenList);
			
			Mockito.when(paginationConfig.toPageRequest(request)).
			thenReturn(PageRequest.of(3, 10, SortOrder.Asc.dir(), "aaa"));
			
			Mockito.when(notamApiTokenRepository.findAll(Mockito.any(NotamApiTokenSpecification.class), 
					Mockito.any(PageRequest.class))).thenReturn(page);
			
			try {
				SearchResponse<NotamApiTokenModel> response = notamApiTokenServiceImpl.searchToken(request);
				
				Assertions.assertNotNull(response);
			} catch (ApiException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Method to test NotamApiTokenServiceImpl.searchToken() method.
	 * 
	 * Method to check whether exception is thrown correctly or not.
	 */
	@DisplayName("Test NotamApiTokenServiceImpl.searchToken()")
	@Test
	public void searchTokenExceptionTest()
	{
		SearchNotamApiTokenRequest request = new SearchNotamApiTokenRequest();
		try (MockedStatic<gov.faa.notam.developerportal.security.SecurityUtil> mocked = Mockito
				.mockStatic(SecurityUtil.class))
		{
			mocked.when(SecurityUtil::getCurrentUserId).thenReturn(Optional.empty());
			mocked.when(SecurityUtil::getCurrentUserRole).thenReturn(UserRole.USER);
			
			ApiException exception = Assertions.assertThrows(ApiException.class, ()->{
				notamApiTokenServiceImpl.searchToken(request);
			});
			
			Assertions.assertTrue(exception.getMessage().contains("Current user id not found"));
			
		}
	}
	
}
