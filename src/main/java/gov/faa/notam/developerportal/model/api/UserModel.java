package gov.faa.notam.developerportal.model.api;

import gov.faa.notam.developerportal.model.entity.UserRole;
import gov.faa.notam.developerportal.model.entity.User;
import lombok.Data;

/**
 * User data returned to the client.
 */
@Data
public class UserModel {
    private Long id;

    private String fullName;

    private String email;

    private UserRole role;

    private String company;

    private String address;

    private String city;

    private String state;

    private String country;

    private String zipCode;

    private String primaryPhone;

    private String alternatePhone;

    private String notamDataIntendedUsage;

    private Boolean isEmailConfirmed;

    private Boolean isApproved;

    private Boolean isEnabled;

    /**
     * Construct from a user entity.
     *
     * @param user - user entity from the persistence layer.
     */
    public UserModel(User user) {
        setId(user.getId());
        setRole((user.getRole()));
        setAddress(user.getAddress());
        setAlternatePhone(user.getAlternatePhone());
        setCity(user.getCity());
        setCompany(user.getCompany());
        setCountry(user.getCountry());
        setEmail(user.getEmail());
        setFullName(user.getFullName());
        setIsApproved(user.getApproved());
        setIsEmailConfirmed(user.isEmailConfirmed());
        setIsEnabled(user.isEnabled());
        setNotamDataIntendedUsage(user.getNotamDataIntendedUsage());
        setPrimaryPhone(user.getPrimaryPhone());
        setState(user.getState());
        setZipCode(user.getZipCode());
    }
}
