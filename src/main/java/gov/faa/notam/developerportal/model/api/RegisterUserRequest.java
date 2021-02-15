package gov.faa.notam.developerportal.model.api;

import javax.validation.constraints.NotBlank;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Request to register a new regular user. Includes additional reCAPTCHA response.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class RegisterUserRequest extends AbstractRegisterRequest {
    @NotBlank
    private String reCaptchaResponse;
}
