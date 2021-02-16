package gov.faa.notam.developerportal.model.api;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Request to search an API token.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SearchNotamApiAccessItemRequest extends AbstractSearchRequest {
    /**
     * The type of the access item
     */
    private NotamApiAccessItemType itemType;

    /**
     * The keyword to be searched
     */
    private String keyword;
}
