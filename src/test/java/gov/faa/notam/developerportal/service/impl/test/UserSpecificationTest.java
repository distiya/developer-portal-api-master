package gov.faa.notam.developerportal.service.impl.test;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import gov.faa.notam.developerportal.model.api.SearchUserRequest;
import gov.faa.notam.developerportal.model.entity.User;
import gov.faa.notam.developerportal.service.impl.UserSpecification;

/**
 * Test class for UserSpecification
 * @author AjithCK
 *
 */
@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
public class UserSpecificationTest
{
 
    
	/**
	 * Class under test
	 */
	UserSpecification userSpecification;
	
	/**
	 * Mocking SearchUserRequest
	 */
	@Mock
	private  SearchUserRequest searchUserRequest;
	
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
	 * Mocking Root<User>
	 */
	@Mock
    private Root<User> personRootMock;
	
	/**
	 * Initialize UserSpecification
	 */
	@BeforeEach
	void setupEach() 
	{
		userSpecification = new UserSpecification(searchUserRequest);
	}
	
	/**
	 * Test method for UserSpecification.toPredicate method
	 */
	@SuppressWarnings({"unchecked" })
	@DisplayName("Test UserSpecification.toPredicate()")
	@Test 
	public void doFilterTest() 
	{ 
		Path<Object> approved = Mockito.mock(Path.class);  
		Path<Object> email    = Mockito.mock(Path.class); 
		Path<Object> fullName = Mockito.mock(Path.class); 
		
		Mockito.when(personRootMock.get("approved")).thenReturn(approved);
		Mockito.when(personRootMock.get("email")).thenReturn(email);
		Mockito.when(personRootMock.get("fullName")).thenReturn(fullName);
		Mockito.when(searchUserRequest.getEmail()).thenReturn("aaa@aaa");
		Mockito.when(searchUserRequest.getIsApproved()).thenReturn(true);
		Mockito.when(searchUserRequest.getName()).thenReturn("aaa");
        
		userSpecification.toPredicate(personRootMock, criteriaQueryMock, criteriaBuilderMock);
	}
	
	/**
	 * Test method for UserSpecification.toPredicate method
	 * This method is to cover branches by making getEmail and getName as null
	 */
	@SuppressWarnings({"unchecked" })
	@DisplayName("Test UserSpecification.toPredicate() - Else check")
	@Test 
	public void doFilter1Test() 
	{ 
		Path<Object> approved = Mockito.mock(Path.class);  
		Mockito.when(personRootMock.get("approved")).thenReturn(approved);
		Mockito.when(searchUserRequest.getEmail()).thenReturn(null);
		Mockito.when(searchUserRequest.getIsApproved()).thenReturn(true);
		Mockito.when(searchUserRequest.getName()).thenReturn(null);
        
		userSpecification.toPredicate(personRootMock, criteriaQueryMock, criteriaBuilderMock);
	}

	/**
	 * Test method for UserSpecification.toPredicate method
	 * This method is to cover branches by making getEmail and isApproved as null
	 */
	@SuppressWarnings({"unchecked" })
	@DisplayName("Test UserSpecification.toPredicate() - Else check")
	@Test 
	public void doFilter2Test() 
	{ 
		Path<Object> fullName = Mockito.mock(Path.class); 
		Mockito.when(personRootMock.get("fullName")).thenReturn(fullName);
		Mockito.when(searchUserRequest.getEmail()).thenReturn(null);
		Mockito.when(searchUserRequest.getIsApproved()).thenReturn(null);
		Mockito.when(searchUserRequest.getName()).thenReturn("aaa");
        
		userSpecification.toPredicate(personRootMock, criteriaQueryMock, criteriaBuilderMock);
	}
	
	/**
	 * Test method for UserSpecification.toPredicate method
	 * This method is to cover branches by making isApproved and getName as null
	 */
	@SuppressWarnings({"unchecked" })
	@DisplayName("Test UserSpecification.toPredicate()")
	@Test 
	public void doFilter3Test() 
	{ 
		Path<Object> email = Mockito.mock(Path.class); 
		Mockito.when(personRootMock.get("email")).thenReturn(email);
		Mockito.when(searchUserRequest.getEmail()).thenReturn("aaa@aaa");
		Mockito.when(searchUserRequest.getIsApproved()).thenReturn(null);
		Mockito.when(searchUserRequest.getName()).thenReturn(null);
        
		userSpecification.toPredicate(personRootMock, criteriaQueryMock, criteriaBuilderMock);
	}
}
