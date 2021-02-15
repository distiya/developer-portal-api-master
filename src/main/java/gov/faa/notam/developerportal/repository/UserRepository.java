package gov.faa.notam.developerportal.repository;

import java.util.Optional;

import gov.faa.notam.developerportal.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    /**
     * Find user by email.
     *
     * @param email - email of the user.
     * @return Optional.empty() if email doesn't exist, or user.deleted is true.
     */
    Optional<User> findByEmail(String email);

    /**
     * Find user by email, including deleted user (user.deleted is true).
     * Using native query to by pass the @Where annotation.
     *
     * @param email - email of the user.
     * @return Optional.empty() if email doesn't exist.
     */
    @Query(value = "select * from \"user\" where email=:email", nativeQuery = true)
    Optional<User> findByEmailIncludingDeleted(@Param("email") String email);
}
