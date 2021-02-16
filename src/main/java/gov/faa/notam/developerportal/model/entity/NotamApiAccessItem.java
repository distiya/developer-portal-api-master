package gov.faa.notam.developerportal.model.entity;

import gov.faa.notam.developerportal.model.api.NotamApiAccessItemType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Data
@EqualsAndHashCode(callSuper = false, of = "id")
@Entity
@Table(name = "notam_api_access_item")
@Where(clause = "not is_deleted")
public class NotamApiAccessItem extends AbstractAuditEntity {
    private static final String SEQUENCE_NAME = "notam_api_access_item_id_seq";

    @Id
    @Column(name = "id")
    @SequenceGenerator(name = SEQUENCE_NAME, sequenceName = SEQUENCE_NAME, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQUENCE_NAME)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "item_type", nullable = false)
    private NotamApiAccessItemType type;

    @Column(name = "version", length = 20, nullable = false)
    private String version;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "change_log", nullable = false)
    private String changeLog;

    @Column(name = "content_type", nullable = false)
    private String contentType;

    @Column(name = "content_length", nullable = false)
    private Integer contentLength;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "content", nullable = false)
    private byte[] content;

    @Column(name = "file_name", nullable = false)
    private String fileName;

}
