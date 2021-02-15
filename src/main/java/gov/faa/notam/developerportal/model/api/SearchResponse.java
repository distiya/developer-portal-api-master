package gov.faa.notam.developerportal.model.api;

import java.util.List;

import lombok.Data;

/**
 * Generic search response.
 *
 * @param <T> type of the items.
 */
@Data
public class SearchResponse<T> {
    /**
     * Total number of items.
     */
    private long totalCount;

    /**
     * The current page of items.
     */
    private List<T> items;
}
