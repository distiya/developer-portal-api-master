package gov.faa.notam.developerportal.configuration;

import gov.faa.notam.developerportal.security.SecurityUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Enables JPA auditing.
 */
@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class JpaConfig {
    /**
     * The auditor aware bean that will return the current user id.
     *
     * @return the auditor aware bean.
     */
    @Bean
    public AuditorAware<Long> auditorAware() {
        return SecurityUtil::getCurrentUserId;
    }
}
