package gov.faa.notam.developerportal.security.test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.passay.PasswordValidator;
import org.springframework.core.io.AbstractResource;
import org.springframework.core.io.Resource;
import org.springframework.test.util.ReflectionTestUtils;
import org.yaml.snakeyaml.Yaml;

import gov.faa.notam.developerportal.security.PasswordPolicy;

/**
 * Test class for PasswordPolicy
 * 
 * @author AjithCK
 *
 */
@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
public class PasswordPolicyTest {

	/**
	 * Class under test
	 */
	PasswordPolicy passwordPolicy;

	/**
	 * Mocking PasswordValidator
	 */
	@Mock
	private PasswordValidator passwordValidator;

	/**
	 * Initialize PasswordPolicy
	 */
	@BeforeEach
	void setupEach() {
		passwordPolicy = new PasswordPolicy();
	}

	/**
	 * Test method for PasswordPolicy.init method.
	 */
	@DisplayName("Test PasswordPolicy.init()")
	@Test
	public void initTest() {
		List<RuleDefinition> ruleDefinitionList = new ArrayList<>();
		RuleDefinition ruleDefinition = new RuleDefinition();
		Map<String, Object> properties = new HashMap<>();
		properties.put("minLength", 123);
		properties.put("maxLength", 123);
		ruleDefinition.setProperties(properties);
		ruleDefinition.setType("Length");

		RuleDefinition ruleDefinition1 = new RuleDefinition();
		Map<String, Object> properties1 = new HashMap<>();
		List<String> values = new ArrayList<>();
		values.add("aaaa");
		properties1.put("words", values);
		ruleDefinition1.setProperties(properties);
		ruleDefinition1.setType("Dictionary");

		RuleDefinition ruleDefinition2 = new RuleDefinition();
		ruleDefinition2.setType("Whitespace");

		RuleDefinition ruleDefinition3 = new RuleDefinition();
		ruleDefinition3.setType("Username");

		RuleDefinition ruleDefinition4 = new RuleDefinition();
		ruleDefinition4.setType("AllowedRegex");
		Map<String, Object> properties4 = new HashMap<>();
		properties4.put("regex", ";");
		ruleDefinition4.setProperties(properties4);

		RuleDefinition ruleDefinition5 = new RuleDefinition();
		Map<String, Object> properties5 = new HashMap<>();
		List<String> values5 = new ArrayList<>();
		values5.add("aaaa");
		properties5.put("characters", "a");
		properties5.put("reportAll", false);
		properties5.put("matchBehavior", "Contains");
		ruleDefinition5.setProperties(properties5);
		ruleDefinition5.setType("AllowedCharacter");

		ruleDefinitionList.add(ruleDefinition);
		ruleDefinitionList.add(ruleDefinition1);
		ruleDefinitionList.add(ruleDefinition2);
		ruleDefinitionList.add(ruleDefinition3);
		ruleDefinitionList.add(ruleDefinition4);
		ruleDefinitionList.add(ruleDefinition5);

		PasswordPolicyConfiguration PasswordPolicyConfiguration = new PasswordPolicyConfiguration();
		PasswordPolicyConfiguration.setRuleDefinitions(ruleDefinitionList);

		Yaml yaml = new Yaml();
		String output = yaml.dump(PasswordPolicyConfiguration);

		InputStream targetStream = new ByteArrayInputStream(output.getBytes());

		Resource resource = new AbstractResource() {
			public String getDescription() {
				return output;
			}

			public InputStream getInputStream() {
				return targetStream;
			}
		};

		ReflectionTestUtils.setField(passwordPolicy, "passwordPolicyConfigurationFile", resource);

		passwordPolicy.init();

	}

	/**
	 * Test method for PasswordPolicy.validatePassword method.
	 */
	@DisplayName("Test PasswordPolicy.validatePassword()")
	@Test
	public void validatePasswordTest()
	{
		List<RuleDefinition> ruleDefinitionList = new ArrayList<>();
		RuleDefinition ruleDefinition = new RuleDefinition();
		Map<String, Object> properties = new HashMap<>();
		properties.put("minLength", 123);
		properties.put("maxLength", 123);
		ruleDefinition.setProperties(properties);
		ruleDefinition.setType("Length");

		RuleDefinition ruleDefinition1 = new RuleDefinition();
		Map<String, Object> properties1 = new HashMap<>();
		List<String> values = new ArrayList<>();
		values.add("aaaa");
		properties1.put("words", values);
		ruleDefinition1.setProperties(properties);
		ruleDefinition1.setType("Dictionary");

		RuleDefinition ruleDefinition2 = new RuleDefinition();
		ruleDefinition2.setType("Whitespace");

		RuleDefinition ruleDefinition3 = new RuleDefinition();
		ruleDefinition3.setType("Username");

		RuleDefinition ruleDefinition4 = new RuleDefinition();
		ruleDefinition4.setType("AllowedRegex");
		Map<String, Object> properties4 = new HashMap<>();
		properties4.put("regex", ";");
		ruleDefinition4.setProperties(properties4);

		RuleDefinition ruleDefinition5 = new RuleDefinition();
		Map<String, Object> properties5 = new HashMap<>();
		List<String> values5 = new ArrayList<>();
		values5.add("aaaa");
		properties5.put("characters", "a");
		properties5.put("reportAll", false);
		properties5.put("matchBehavior", "Contains");
		ruleDefinition5.setProperties(properties5);
		ruleDefinition5.setType("AllowedCharacter");

		ruleDefinitionList.add(ruleDefinition);
		ruleDefinitionList.add(ruleDefinition1);
		ruleDefinitionList.add(ruleDefinition2);
		ruleDefinitionList.add(ruleDefinition3);
		ruleDefinitionList.add(ruleDefinition4);
		ruleDefinitionList.add(ruleDefinition5);

		PasswordPolicyConfiguration PasswordPolicyConfiguration = new PasswordPolicyConfiguration();
		PasswordPolicyConfiguration.setRuleDefinitions(ruleDefinitionList);

		Yaml yaml = new Yaml();
		String output = yaml.dump(PasswordPolicyConfiguration);

		InputStream targetStream = new ByteArrayInputStream(output.getBytes());

		Resource resource = new AbstractResource() {
			public String getDescription() {
				return output;
			}

			public InputStream getInputStream() {
				return targetStream;
			}
		};

		ReflectionTestUtils.setField(passwordPolicy, "passwordPolicyConfigurationFile", resource);

		passwordPolicy.init();

		boolean returnValue = passwordPolicy.validatePassword("aa", "bb");

		Assertions.assertEquals(returnValue, false);
	}

	/**
	 * Test method for PasswordPolicy.init method.
	 * This method is to check whether exception is thrown or not.
	 */
	@DisplayName("Test PasswordPolicy.init()")
	@Test
	public void initExceptionTest()
	{
		List<RuleDefinition> ruleDefinitionList = new ArrayList<>();
		RuleDefinition       ruleDefinition     = new RuleDefinition();
		Map<String, Object>  properties         = new HashMap<>();
		
		properties.put("minLength", 123);
		properties.put("maxLength", 123);
		ruleDefinition.setProperties(properties);
		ruleDefinition.setType("Lengthhh");

		ruleDefinitionList.add(ruleDefinition);

		PasswordPolicyConfiguration PasswordPolicyConfiguration = new PasswordPolicyConfiguration();
		PasswordPolicyConfiguration.setRuleDefinitions(ruleDefinitionList);

		Yaml yaml = new Yaml();
		String output = yaml.dump(PasswordPolicyConfiguration);

		InputStream targetStream = new ByteArrayInputStream(output.getBytes());

		Resource resource = new AbstractResource() {
			public String getDescription() {
				return output;
			}

			public InputStream getInputStream() {
				return targetStream;
			}
		};

		ReflectionTestUtils.setField(passwordPolicy, "passwordPolicyConfigurationFile", resource);

		IllegalStateException exception = Assertions.assertThrows(IllegalStateException.class, () -> {
			passwordPolicy.init();
		});
		
		Assertions.assertTrue(exception.getMessage().contains("Unknown rule type: Lengthhh"));
	}
	
	/**
	 * Test method for PasswordPolicy.init method.
	 * This method is to check whether exception is thrown or not.
	 */
	@DisplayName("Test PasswordPolicy.init()")
	@Test 
	public void initException1Test() 
	{
		List<RuleDefinition> ruleDefinitionList = new ArrayList<>();
		RuleDefinition       ruleDefinition     = new RuleDefinition();
		Map<String, Object>  properties         = new HashMap<>();
		
		properties.put("minLength", 123);
		properties.put("maxLength", 123);
		ruleDefinition.setProperties(properties);
		ruleDefinition.setType(null);
		
		ruleDefinitionList.add(ruleDefinition);
		
		PasswordPolicyConfiguration PasswordPolicyConfiguration = new PasswordPolicyConfiguration();
		PasswordPolicyConfiguration.setRuleDefinitions(ruleDefinitionList);
		
		Yaml yaml = new Yaml();
	    String output = yaml.dump(PasswordPolicyConfiguration);
	    
	    InputStream targetStream = new ByteArrayInputStream(output.getBytes());
		
		Resource resource =  new AbstractResource() 
		{
			public String getDescription()
			{
				return output;
			}
			public InputStream getInputStream() {
				return targetStream;
			}
		};
		
		ReflectionTestUtils.setField(passwordPolicy, "passwordPolicyConfigurationFile", resource);
		
		IllegalStateException exception = Assertions.assertThrows(IllegalStateException.class, ()->{
			passwordPolicy.init();
		});
		
		Assertions.assertTrue(exception.getMessage().contains("Missing required property: type"));
	}

	/**
	 * Inner class RuleDefinition
	 *
	 */
	public static class RuleDefinition {
		private String type;

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public Map<String, Object> getProperties() {
			return properties;
		}

		public void setProperties(Map<String, Object> properties) {
			this.properties = properties;
		}

		private Map<String, Object> properties;
	}

	/**
	 * Inner class PasswordPolicyConfiguration
	 *
	 */
	public static class PasswordPolicyConfiguration {
		private List<RuleDefinition> ruleDefinitions;

		public List<RuleDefinition> getRuleDefinitions() {
			return ruleDefinitions;
		}

		public void setRuleDefinitions(List<RuleDefinition> ruleDefinitions) {
			this.ruleDefinitions = ruleDefinitions;
		}
	}

}
