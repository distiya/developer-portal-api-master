package gov.faa.notam.developerportal.repository;

import gov.faa.notam.developerportal.model.entity.HourlyAPIUsage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface HourlyAPIUsageRepository extends JpaRepository<HourlyAPIUsage, Long>, JpaSpecificationExecutor<HourlyAPIUsage> {

}
