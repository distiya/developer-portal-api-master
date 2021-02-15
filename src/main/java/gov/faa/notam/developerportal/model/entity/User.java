package gov.faa.notam.developerportal.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Data
@EqualsAndHashCode(callSuper = false, of = "id")
@Entity
@Table(name = "\"user\"")
@TypeDef(name = "user_role", typeClass = PostgreSQLEnumType.class)
@Where(clause = "not is_deleted")
public class User extends AbstractAuditEntity {
    private static final String SEQUENCE_NAME = "user_id_seq";

    @Id
    @Column(name = "id")
    @SequenceGenerator(name = SEQUENCE_NAME, sequenceName = SEQUENCE_NAME, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQUENCE_NAME)
    private Long id;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    @Type(type = "user_role")
    private UserRole role;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "email")
    private String email;

    @Column(name = "password_hash")
    private String passwordHash;

    @Column(name = "company")
    private String company;

    @Column(name = "address")
    private String address;

    @Column(name = "city")
    private String city;

    @Column(name = "state")
    private String state;

    @Column(name = "country")
    private String country;

    @Column(name = "zip_code")
    private String zipCode;

    @Column(name = "primary_phone")
    private String primaryPhone;

    @Column(name = "alternate_phone")
    private String alternatePhone;

    @Column(name = "notam_data_intended_usage")
    private String notamDataIntendedUsage;

    @Column(name = "email_confirmation_code")
    private String emailConfirmationCode;

    @Column(name = "password_reset_token")
    private String passwordResetToken;

    @Column(name = "is_email_confirmed")
    private boolean emailConfirmed;

    @Column(name = "is_approved")
    private Boolean approved;

    @Column(name = "is_enabled")
    private boolean enabled;
}
