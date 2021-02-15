package gov.faa.notam.developerportal.model.api;

import javax.validation.constraints.NotBlank;

import lombok.Data;

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
