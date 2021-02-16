package gov.faa.notam.developerportal.model.api;

import lombok.Data;

import java.util.Date;

@Data
public class UserRequestModel {

    /**
     * The id of the model
     */
    private Long id;

    /**
     * The full name of the user
     */
    private String fullName;

    /**
     * The address of the user
     */
    private String address;

    /**
     * The city of the user
     */
    private String city;

    /**
     * The state of the user
     */
    private String state;

    /**
     * The country of the user
     */
    private String country;
    /**
     * The created date
     */
    private Date createdDate;

    /**
     * The verification link
     */
    private String verificationLink;
}
