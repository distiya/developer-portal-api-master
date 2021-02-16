package gov.faa.notam.developerportal.repository;

import gov.faa.notam.developerportal.model.entity.NotamApiAccessItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface NotamApiAccessItemRepository
        extends JpaRepository<NotamApiAccessItem, Long>, JpaSpecificationExecutor<NotamApiAccessItem> { }
