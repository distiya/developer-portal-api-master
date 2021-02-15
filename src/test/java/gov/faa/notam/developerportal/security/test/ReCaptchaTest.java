package gov.faa.notam.developerportal.security.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import gov.faa.notam.developerportal.security.ReCaptcha;

/**
 * Test class for ReCaptcha
 * @author AjithCK
 *
 */
@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
public class ReCaptchaTest
{
	/**
	 * Class under test
	 */
	ReCaptcha reCaptcha;

	/**
	 * Initialize ReCaptcha and set class variables.
	 */
	@BeforeEach
    void setupEach()
	{
		reCaptcha = new ReCaptcha();
		ReflectionTestUtils.setField(reCaptcha, "reCaptchaSecretKey", "key");
	}
	
	/**
	 * Test method for ReCaptchaTest.verifyResponse method.
	 */
	@DisplayName("Test ReCaptchaTest.verifyResponse()")
	@Test
	public void verifyResponseTest()
	{
		boolean returnValue = reCaptcha.verifyResponse("key");
		
		Assertions.assertEquals(returnValue, false);
	}
}
