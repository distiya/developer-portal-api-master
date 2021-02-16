package gov.faa.notam.developerportal.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false, of = "id")
@Entity
@Table(name = "activity")
@Where(clause = "not is_deleted")
public class Activity extends AbstractAuditEntity {
    private static final String SEQUENCE_NAME = "activity_id_seq";

    @Id
    @Column(name = "id")
    @SequenceGenerator(name = SEQUENCE_NAME, sequenceName = SEQUENCE_NAME, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQUENCE_NAME)
    private Long id;

    @Column(name = "user_id")
    private Long userID;

    @Column(name = "date_time_hour")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateTimeHour;

    @Column(name = "name")
    private String name;

    @Column(name = "type", length = 45)
    private String type;

    @Column(name = "status", length = 45)
    private String status;

}
