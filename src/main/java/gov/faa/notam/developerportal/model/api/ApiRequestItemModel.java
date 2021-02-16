package gov.faa.notam.developerportal.model.api;

import lombok.Data;

import java.util.Date;

@Data
public class ApiRequestItemModel {

    /**
     * The created date
     */
    private Date date;

    /**
     * Number of request
     */
    private Long num;

}
