package gov.faa.notam.developerportal.model.api;

import org.springframework.data.domain.Sort;

/**
 * Sort order enum submitted by the client.
 */
public enum SortOrder {
    Asc,
    Desc;

    /**
     * Convert to spring data sort direction.
     *
     * @return the corresponding direction.
     */
    public Sort.Direction dir() {
        return this == Desc ? Sort.Direction.DESC : Sort.Direction.ASC;
    }
}
