package gov.faa.notam.developerportal.model.api;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Represents a success login response sent to the client.
 * <p>
 * Corresponds to the #/components/LoginResultModel from swagger.json.
 */
@Data
@AllArgsConstructor
public class LoginResponse {
    /**
     * The generated JWT token.
     */
    private String token;
}
