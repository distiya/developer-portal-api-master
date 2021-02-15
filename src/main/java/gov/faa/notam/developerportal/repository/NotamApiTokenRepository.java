package gov.faa.notam.developerportal.repository;

import gov.faa.notam.developerportal.model.entity.NotamApiToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface NotamApiTokenRepository
        extends JpaRepository<NotamApiToken, Long>, JpaSpecificationExecutor<NotamApiToken> { }
