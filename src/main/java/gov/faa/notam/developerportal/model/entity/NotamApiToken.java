package gov.faa.notam.developerportal.model.entity;

import gov.faa.notam.developerportal.model.api.NotamApiTokenStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Data
@EqualsAndHashCode(callSuper = false, of = "id")
@Entity
@Table(name = "notam_api_token")
@Where(clause = "not is_deleted")
public class NotamApiToken extends AbstractAuditEntity {
    private static final String SEQUENCE_NAME = "notam_api_token_id_seq";

    @Id
    @Column(name = "id")
    @SequenceGenerator(name = SEQUENCE_NAME, sequenceName = SEQUENCE_NAME, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQUENCE_NAME)
    private Long id;

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "name")
    private String name;

    @Column(name = "key")
    private String key;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 45)
    private NotamApiTokenStatus status;

    @Column(name = "is_enabled_by_user")
    private boolean enabledByUser;

    @Column(name = "is_enabled_by_admin")
    private boolean enabledByAdmin;
}
