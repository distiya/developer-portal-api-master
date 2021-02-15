package gov.faa.notam.developerportal.model.api;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

/**
 * Request to register a new regular user. Includes additional reCAPTCHA response.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class RegisterUserRequest extends AbstractRegisterRequest {
    @NotBlank
    private String reCaptchaResponse;
}
