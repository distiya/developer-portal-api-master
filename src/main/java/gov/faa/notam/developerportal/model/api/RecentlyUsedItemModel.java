package gov.faa.notam.developerportal.model.api;

import lombok.Data;

@Data
public class RecentlyUsedItemModel {

    /**
     * The name of the API
     */
    private String apiName;

    /**
     * The requestor
     */
    private String requestor;

    /**
     * The request status
     */
    private String status;

    /**
     * The retention time in minutes
     */
    private Integer retentionTime;

    /**
     * The response time in milliseconds
     */
    private Long responseTime;

}
