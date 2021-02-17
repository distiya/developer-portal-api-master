package gov.faa.notam.developerportal.model.api;

import gov.faa.notam.developerportal.model.entity.UserCount;
import lombok.Data;

@Data
public class UserCountPerCountryModel {

    /**
     * The country name
     */
    private String country;

    /**
     * The total user count for the given country
     */
    private Integer userCount;

    public UserCountPerCountryModel(UserCount userCount){
        setCountry(userCount.getCountry());
        setUserCount(userCount.getUserCount());
    }

}
