package gov.faa.notam.developerportal.model.api;

import lombok.Data;

import java.util.Date;

@Data
public class ActiveUsageItemModel {

    /**
     * The access key
     */
    private String accessKey;

    /**
     * The requestor
     */
    private String requestor;

    /**
     * The created date
     */
    private Date createdDate;

}
