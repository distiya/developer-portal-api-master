package gov.faa.notam.developerportal.model.api;

import lombok.Data;

@Data
public class ApiActivityModel {

    /**
     * Number of active activities
     */
    private Long active;

    /**
     * Number of inactive activities
     */
    private Long inactive;

    /**
     * Number of requests
     */
    private Long requests;

    /**
     * Number of error activities
     */
    private Long errors;

}
