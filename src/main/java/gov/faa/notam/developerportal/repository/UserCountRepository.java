package gov.faa.notam.developerportal.repository;

import gov.faa.notam.developerportal.model.entity.UserCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UserCountRepository extends JpaRepository<UserCount, Long>, JpaSpecificationExecutor<UserCount> {

}
