package gov.faa.notam.developerportal.model.api;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import lombok.Data;

/**
 * Request to send a password reset email.
 */
@Data
public class ForgotPasswordRequest {
    @NotBlank
    @Email
    private String email;
}
