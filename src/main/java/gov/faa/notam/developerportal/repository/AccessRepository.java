package gov.faa.notam.developerportal.repository;

import gov.faa.notam.developerportal.model.entity.Access;
import gov.faa.notam.developerportal.model.entity.AccessStatistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AccessRepository extends JpaRepository<Access, Long>, JpaSpecificationExecutor<Access> {

    @Query("select new gov.faa.notam.developerportal.model.entity.AccessStatistics(u.status,count(u.id) as statusGroupCount) from Access u group by u.status")
    List<AccessStatistics> countAccessByStatus();

}
