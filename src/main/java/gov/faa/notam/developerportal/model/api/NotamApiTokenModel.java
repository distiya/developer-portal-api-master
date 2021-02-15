package gov.faa.notam.developerportal.model.api;

import gov.faa.notam.developerportal.model.entity.NotamApiToken;
import lombok.Data;

import java.util.Date;

/**
 * Notam API token returned to the frontend.
 */
@Data
public class NotamApiTokenModel {
    /**
     * id of the token.
     */
    private Long id;

    /**
     * name of the token.
     */
    private String name;

    /**
     * Base64 encoded key.
     */
    private String key;

    /**
     * The status of the token
     */
    private NotamApiTokenStatus status;

    /**
     * Enabled by the owner.
     */
    private Boolean isEnabledByUser;

    /**
     * Enabled by admin.
     */
    private Boolean isEnabledByAdmin;

    /**
     * The created date of the token
     */
    private Date createdDate;

    /**
     * The activity usage of last twenty four hours
     */
    private LastTwentyFourHoursActivityModel last24hoursActivityUsage;

    /**
     * Construct from a token entity.
     *
     * @param token - token entity returned by the persistence layer.
     */
    public NotamApiTokenModel(NotamApiToken token) {
        setId(token.getId());
        setName(token.getName());
        setKey(token.getKey());
        setIsEnabledByAdmin(token.isEnabledByAdmin());
        setIsEnabledByUser(token.isEnabledByUser());
        setCreatedDate(token.getCreatedAt());
        setStatus(token.getStatus());
    }
}