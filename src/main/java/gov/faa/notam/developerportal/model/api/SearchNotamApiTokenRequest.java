package gov.faa.notam.developerportal.model.api;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Request to search an API token.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SearchNotamApiTokenRequest extends AbstractSearchRequest {
    /**
     * id of the owner of the tokens.
     */
    private Long userId;

    /**
     * name of the tokens to match.
     */
    private String name;
}
