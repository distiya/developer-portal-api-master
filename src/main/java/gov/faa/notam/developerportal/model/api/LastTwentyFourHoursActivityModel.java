package gov.faa.notam.developerportal.model.api;

import lombok.Data;

/**
 * Data model to represent last 24 hours activity usage
 */
@Data
public class LastTwentyFourHoursActivityModel {

    /**
     * Total number of successful activity
     */
    private Long success;

    /**
     * Total number of error activity
     */
    private Long error;

}
