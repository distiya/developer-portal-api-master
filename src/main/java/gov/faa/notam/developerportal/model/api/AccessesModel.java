package gov.faa.notam.developerportal.model.api;

import lombok.Data;

@Data
public class AccessesModel {

    /**
     * Number of active activities
     */
    private Long successes;

    /**
     * Number of requests
     */
    private Long requests;

    /**
     * Number of error 904c
     */
    private Long errors904c;

    /**
     * Number of other errors
     */
    private Long othererrors;

}
