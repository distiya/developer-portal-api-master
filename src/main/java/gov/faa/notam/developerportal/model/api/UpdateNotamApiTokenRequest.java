package gov.faa.notam.developerportal.model.api;

import lombok.Data;

/**
 * Request to update an API token.
 */
@Data
public class UpdateNotamApiTokenRequest {
    private String name;

    /**
     * The status of the token
     */
    private NotamApiTokenStatus status;
}
