package gov.faa.notam.developerportal.service.impl;

import gov.faa.notam.developerportal.model.api.SearchUserRequest;
import gov.faa.notam.developerportal.model.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

/**
 * User specification for searching users using the search user request.
 */
@AllArgsConstructor
public class UserSpecification implements Specification<User> {

    /**
     * The search user request.
     */
    private final SearchUserRequest searchUserRequest;

    @Override
    public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        List<Predicate> predicates = new ArrayList<>();

        if (searchUserRequest.getIsApproved() != null) {
            predicates.add(builder.equal(root.<Boolean>get("approved"), searchUserRequest.getIsApproved()));
        }

        if (searchUserRequest.getEmail() != null) {
            predicates.add(builder.like(builder.upper(root.get("email")),
                    "%" + searchUserRequest.getEmail().toUpperCase() + "%"));
        }

        if (searchUserRequest.getName() != null) {
            predicates.add(builder.like(builder.upper(root.get("fullName")),
                    "%" + searchUserRequest.getName().toUpperCase() + "%"));
        }

        return builder.and(predicates.toArray(new Predicate[0]));
    }
}
