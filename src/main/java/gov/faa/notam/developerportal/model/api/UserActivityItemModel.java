package gov.faa.notam.developerportal.model.api;

import lombok.Data;

import java.util.Date;

@Data
public class UserActivityItemModel {

    /**
     * The access key
     */
    private String accessKey;

    /**
     * The status of the activity
     */
    private UserActivityStatus status;

    /**
     * The created date
     */
    private Date createdDate;

}
