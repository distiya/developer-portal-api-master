package gov.faa.notam.developerportal.service.impl;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

import gov.faa.notam.developerportal.model.api.SearchNotamApiTokenRequest;
import gov.faa.notam.developerportal.model.entity.NotamApiToken;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

/**
 * JPA specification for searching notam API token.
 */
@RequiredArgsConstructor
public class NotamApiTokenSpecification implements Specification<NotamApiToken> {
    /**
     * The search request to build the predicates from.
     */
    private final SearchNotamApiTokenRequest request;

    @Override
    public Predicate toPredicate(Root<NotamApiToken> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();

        if (request.getName() != null) {
            predicates.add(criteriaBuilder.like(criteriaBuilder.upper(root.get("name")),
                    "%" + request.getName().toUpperCase() + "%"));
        }

        if (request.getUserId() != null) {
            predicates.add(criteriaBuilder.equal(root.get("user").<Long>get("id"), request.getUserId()));
        }

        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}
