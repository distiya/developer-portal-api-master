package gov.faa.notam.developerportal.model.api;

import lombok.Data;

/**
 * Common search fields.
 */
@Data
public abstract class AbstractSearchRequest {
    /**
     * Sort-by field.
     */
    private String sortBy;

    /**
     * Sort order.
     */
    private SortOrder sortOrder;

    /**
     * 0-based offset.
     */
    private Integer offset;

    /**
     * Page size.
     */
    private Integer limit;
}
