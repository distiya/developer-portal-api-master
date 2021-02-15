package gov.faa.notam.developerportal.model.api;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Request to search users.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SearchUserRequest extends AbstractSearchRequest {
    /**
     * Name to match.
     */
    private String name;

    /**
     * Email to match.
     */
    private String email;

    /**
     * Filter by isApproved.
     */
    private Boolean isApproved;
}
