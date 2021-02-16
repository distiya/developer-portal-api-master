package gov.faa.notam.developerportal.model.api;

import lombok.Data;

import java.util.Date;

@Data
public class AccessEventModel {

    /**
     * Event ID
     */
    private Long id;

    /**
     * The source of the access
     */
    private String source;

    /**
     * The created date
     */
    private Date date_and_time;

    /**
     * The status of the access event
     */
    private String status;
}
