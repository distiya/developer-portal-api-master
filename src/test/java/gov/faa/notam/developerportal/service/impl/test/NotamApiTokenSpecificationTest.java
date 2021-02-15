package gov.faa.notam.developerportal.service.impl.test;

import gov.faa.notam.developerportal.model.api.SearchNotamApiTokenRequest;
import gov.faa.notam.developerportal.model.entity.NotamApiToken;
import gov.faa.notam.developerportal.service.impl.NotamApiTokenSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;

/**
 * Test class for NotamApiTokenSpecification
 * @author AjithCK
 *
 */
@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
public class NotamApiTokenSpecificationTest 
{
	/**
	 * Class under test
	 */
	NotamApiTokenSpecification notamApiTokenSpecification;
	
	/**
	 * Mocking SearchNotamApiTokenRequest
	 */
	@Mock
	private  SearchNotamApiTokenRequest request;
	
	/**
	 * Mocking CriteriaBuilder
	 */
	@Mock
	private CriteriaBuilder criteriaBuilderMock;
    
	/**
	 * Mocking CriteriaQuery
	 */
	@SuppressWarnings("rawtypes")
	@Mock
    private CriteriaQuery criteriaQueryMock;
     
	/**
	 * Mocking Root<NotamApiToken>
	 */
	@Mock
    private Root<NotamApiToken> personRootMock;
	
	/**
	 * Initialize NotamApiTokenSpecification
	 */
	@BeforeEach
	void setupEach() 
	{
		notamApiTokenSpecification = new NotamApiTokenSpecification(request);
	}
	
	/**
	 * Test method for NotamApiTokenSpecification.toPredicate method
	 */
	@SuppressWarnings("unchecked")
	@DisplayName("Test NotamApiTokenSpecification.toPredicate()")
	@Test 
	public void toPredicateTest() 
	{ 
		Path<Object> name   = Mockito.mock(Path.class);  
		Path<Object> user   = Mockito.mock(Path.class); 
		Path<Object> userId = Mockito.mock(Path.class); 
		
		Mockito.when(personRootMock.get("name")).thenReturn(name);
		Mockito.when(personRootMock.get("user")).thenReturn(user);
		Mockito.when(personRootMock.get("user").get("id")).thenReturn(userId);
		
		Mockito.when(request.getName()).thenReturn("aaa");
		Mockito.when(request.getUserId()).thenReturn(123L);
		
		notamApiTokenSpecification.toPredicate(personRootMock,  criteriaQueryMock, criteriaBuilderMock);
	}
	
	/**
	 * Test method for NotamApiTokenSpecification.toPredicate method
	 * Method to cover the code branch getName returns null to make if condition false
	 */
	@SuppressWarnings({"unchecked" })
	@DisplayName("Test NotamApiTokenSpecification.toPredicate() else condition check")
	@Test 
	public void toPredicate1Test() 
	{ 
		Path<Object> user = Mockito.mock(Path.class); 
		Path<Object> userId = Mockito.mock(Path.class); 
		
		Mockito.when(personRootMock.get("user")).thenReturn(user);
		Mockito.when(personRootMock.get("user").get("id")).thenReturn(userId);
		
		Mockito.when(request.getName()).thenReturn(null);
		Mockito.when(request.getUserId()).thenReturn(123L);

		
		notamApiTokenSpecification.toPredicate(personRootMock,  criteriaQueryMock, criteriaBuilderMock);
	}
	
	/**
	 * Test method for NotamApiTokenSpecification.toPredicate method
	 * Method to cover branch coverage. getUserId returns null to make if check false.
	 */
	@SuppressWarnings({"unchecked" })
	@DisplayName("Test NotamApiTokenSpecification.toPredicate() else condition check")
	@Test 
	public void toPredicate2Test() 
	{ 
		Path<Object> name = Mockito.mock(Path.class);  
		
		Mockito.when(personRootMock.get("name")).thenReturn(name);
		
		Mockito.when(request.getUserId()).thenReturn(null);
		Mockito.when(request.getName()).thenReturn("aaa");

		
		notamApiTokenSpecification.toPredicate(personRootMock,  criteriaQueryMock, criteriaBuilderMock);
	}
}
