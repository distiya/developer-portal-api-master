package gov.faa.notam.developerportal.model.api;

import lombok.Data;

/**
 * Request for creating a new api token.
 */
@Data
public class CreateNotamApiTokenRequest {
    /**
     * Name of the token.
     */
    private String name;
}
