package gov.faa.notam.developerportal.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false, of = "id")
@Entity
@Table(name = "hourly_api_usage")
@Where(clause = "not is_deleted")
public class HourlyAPIUsage extends AbstractAuditEntity {
    private static final String SEQUENCE_NAME = "hourly_api_usage_id_seq";

    @Id
    @Column(name = "id")
    @SequenceGenerator(name = SEQUENCE_NAME, sequenceName = SEQUENCE_NAME, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQUENCE_NAME)
    private Long id;

    @Column(name = "date_time_hour", nullable = false, unique = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateTimeHour;

    @Column(name = "request_count", nullable = false)
    private Integer requestCount;

    @Column(name = "returned_notam_count", nullable = false)
    private Integer returnedNotamCount;

}
