package gov.faa.notam.developerportal.model.api;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * Represents a login request from the client.
 * <p>
 * Corresponds to the #/components/LoginModel object from swagger.json.
 */
@Data
public class LoginRequest {
    @NotBlank(message = "Email should not be blank")
    @Email(message = "Invalid email")
    private String email;

    @NotBlank(message = "Password should not be blank")
    private String password;
}
