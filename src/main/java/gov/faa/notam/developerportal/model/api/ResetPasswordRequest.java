package gov.faa.notam.developerportal.model.api;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * Request to reset password using a password reset token.
 */
@Data
public class ResetPasswordRequest {
    @NotBlank
    private String newPassword;

    @NotBlank
    private String token;
}
