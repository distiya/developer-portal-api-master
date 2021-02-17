package gov.faa.notam.developerportal.model.api;

import gov.faa.notam.developerportal.model.entity.Access;
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
    private ApiAccessStatus status;

    public AccessEventModel(Access access){
        setId(access.getId());
        setDate_and_time(access.getDateAndTime());
        setSource(access.getSource());
        setStatus(access.getStatus());
    }
}
