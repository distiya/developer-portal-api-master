package gov.faa.notam.developerportal.model.entity;

import gov.faa.notam.developerportal.model.api.ApiActivityStatus;
import gov.faa.notam.developerportal.model.api.NotamApiAccessItemType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false, of = "id")
@Entity
@Table(name = "activity")
public class Activity{
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

    @Enumerated(EnumType.STRING)
    @Column(name = "type", length = 45)
    NotamApiAccessItemType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 45)
    private ApiActivityStatus status;

}
