package gov.faa.notam.developerportal.configuration;

import gov.faa.notam.developerportal.model.api.AbstractSearchRequest;
import gov.faa.notam.developerportal.model.api.SortOrder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Pagination configuration component.
 */
@Component
@Getter
@Setter
public class PaginationConfig {
    /**
     * Default page size.
     */
    @Value("${pagination.limit.default}")
    private int defaultPageSize;

    /**
     * Maximum page size.
     */
    @Value("${pagination.limit.max}")
    private int maxPageSize;

    /**
     * Generate page request from the abstract search request object.
     *
     * @param request - the request object.
     * @return a JPA page request.
     */
    public PageRequest toPageRequest(AbstractSearchRequest request) {
        int pageSize = Math.max(1, Math.min(Optional.ofNullable(request.getLimit()).orElseGet(this::getDefaultPageSize),
                getMaxPageSize()));
        int offset = Optional.ofNullable(request.getOffset()).orElse(0);
        SortOrder sortOrder = Optional.ofNullable(request.getSortOrder()).orElse(SortOrder.Asc);
        String sortBy = Optional.ofNullable(request.getSortBy()).orElse("id");
        return PageRequest.of(offset / pageSize, pageSize, sortOrder.dir(), sortBy);
    }
}
