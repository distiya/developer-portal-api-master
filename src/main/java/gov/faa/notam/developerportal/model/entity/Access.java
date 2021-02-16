package gov.faa.notam.developerportal.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false, of = "id")
@Entity
@Table(name = "access")
@Where(clause = "not is_deleted")
public class Access extends AbstractAuditEntity {
    private static final String SEQUENCE_NAME = "access_id_seq";

    @Id
    @Column(name = "id")
    @SequenceGenerator(name = SEQUENCE_NAME, sequenceName = SEQUENCE_NAME, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQUENCE_NAME)
    private Long id;

    @Column(name = "date_and_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateAndTime;

    @Column(name = "source")
    private String source;

    @Column(name = "status", length = 45)
    private String status;

}
