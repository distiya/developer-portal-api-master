package gov.faa.notam.developerportal.model.api;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * Represents a change password request from the client.
 * <p>
 * Corresponds to the <code>#/components/ChangePasswordModel</code> from swagger.json.
 */
@Data
public class ChangePasswordRequest {
    @NotBlank
    private String currentPassword;

    @NotBlank
    private String newPassword;
}
