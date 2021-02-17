package gov.faa.notam.developerportal.model.api;

import gov.faa.notam.developerportal.model.entity.Activity;
import lombok.Data;

import java.util.Date;

@Data
public class ActivityEventModel {

    /**
     * Event ID
     */
    private Long id;

    /**
     * The name of the api/sdk
     */
    private String name;

    /**
     * The type of the api/sdk
     */
    private NotamApiAccessItemType type;

    /**
     * The created date
     */
    private Date date_and_time;

    /**
     * The status of the activity event
     */
    private ApiActivityStatus status;

    public ActivityEventModel(Activity activity){
        setId(activity.getId());
        setName(activity.getName());
        setType(activity.getType());
        setDate_and_time(activity.getDateTimeHour());
        setStatus(activity.getStatus());
    }
}
