package gov.faa.notam.developerportal.model.api;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * User register request. Common fields for both admin and regular user.
 */
@Data
public abstract class AbstractRegisterRequest {
    @NotBlank
    private String fullName;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;

    @NotBlank
    private String company;

    @NotBlank
    private String address;

    @NotBlank
    private String city;

    @NotBlank
    private String state;

    @NotBlank
    private String country;

    @NotBlank
    private String zipCode;

    @NotBlank
    private String primaryPhone;

    private String alternatePhone;

    private String notamDataIntendedUsage;
}
