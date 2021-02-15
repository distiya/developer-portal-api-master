package gov.faa.notam.developerportal.model.api;

import javax.validation.constraints.Pattern;

import lombok.Data;

/**
 * Request to update a user.
 */
@Data
public class UpdateUserRequest {
    @Pattern(regexp = ".*\\S.*")
    private String fullName;

    @Pattern(regexp = ".*\\S.*")
    private String password;

    @Pattern(regexp = ".*\\S.*")
    private String company;

    @Pattern(regexp = ".*\\S.*")
    private String address;

    @Pattern(regexp = ".*\\S.*")
    private String city;

    @Pattern(regexp = ".*\\S.*")
    private String state;

    @Pattern(regexp = ".*\\S.*")
    private String country;

    @Pattern(regexp = ".*\\S.*")
    private String zipCode;

    @Pattern(regexp = ".*\\S.*")
    private String primaryPhone;

    @Pattern(regexp = ".*\\S.*")
    private String alternatePhone;

    @Pattern(regexp = ".*\\S.*")
    private String notamDataIntendedUsage;
}
