package gov.faa.notam.developerportal.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Data
@EqualsAndHashCode(callSuper = false, of = "id")
@Entity
@Table(name = "user_count_per_country")
public class UserCount extends AbstractAuditEntity {
    private static final String SEQUENCE_NAME = "user_count_per_country_id_seq";

    @Id
    @Column(name = "id")
    @SequenceGenerator(name = SEQUENCE_NAME, sequenceName = SEQUENCE_NAME, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQUENCE_NAME)
    private Long id;

    @Column(name = "country", nullable = false, unique = true)
    private String country;

    @Column(name = "user_count", nullable = false)
    private Integer userCount;

}
