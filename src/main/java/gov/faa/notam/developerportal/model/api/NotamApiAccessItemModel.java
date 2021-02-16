package gov.faa.notam.developerportal.model.api;

import gov.faa.notam.developerportal.model.entity.NotamApiAccessItem;
import lombok.Data;

import java.util.Date;

@Data
public class NotamApiAccessItemModel {

    /**
     * The id of the access item
     */
    private Long id;

    /**
     * The type of the access item
     */
    private NotamApiAccessItemType itemType;

    /**
     * The version
     */
    private String version;

    /**
     * The description
     */
    private String description;

    /**
     * The change log
     */
    private String changeLog;

    /**
     * The content type of the file
     */
    private String contentType;

    /**
     * The content length of the file
     */
    private Integer contentLength;

    /**
     * The name of the file
     */
    private String fileName;

    /**
     * The created date
     */
    private Date createdDate;

    /**
     * The updated date
     */
    private Date updatedDate;

    public NotamApiAccessItemModel(Long id, NotamApiAccessItemType type, String version, String description, String changeLog, String contentType, Integer contentLength, String fileName, Date createdDate, Date updatedDate){
        setId(id);
        setItemType(type);
        setVersion(version);
        setDescription(description);
        setChangeLog(changeLog);
        setContentType(contentType);
        setContentLength(contentLength);
        setFileName(fileName);
        setCreatedDate(createdDate);
        setUpdatedDate(updatedDate);
    }

    public NotamApiAccessItemModel(NotamApiAccessItem notamApiAccessItem){
        setId(notamApiAccessItem.getId());
        setItemType(notamApiAccessItem.getType());
        setVersion(notamApiAccessItem.getVersion());
        setDescription(notamApiAccessItem.getDescription());
        setChangeLog(notamApiAccessItem.getChangeLog());
        setContentType(notamApiAccessItem.getContentType());
        setContentLength(notamApiAccessItem.getContentLength());
        setFileName(notamApiAccessItem.getFileName());
        setCreatedDate(notamApiAccessItem.getCreatedAt());
        setUpdatedDate(notamApiAccessItem.getUpdatedAt());
    }

}
