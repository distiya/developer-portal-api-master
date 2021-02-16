package gov.faa.notam.developerportal.model.api;

import lombok.Data;

import java.util.Date;

@Data
public class HourlyApiUsageModel {

    /**
     * The date time hour of the aggregated data
     */
    private Date dateTimeHour;

    /**
     * The total API request count for the given api
     */
    private Integer requestCount;

    /**
     * The total returned count of NOTAMs for the given hour
     */
    private Integer returnedNotamCount;
}
