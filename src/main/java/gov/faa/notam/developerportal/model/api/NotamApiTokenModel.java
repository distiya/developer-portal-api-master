package gov.faa.notam.developerportal.model.api;

import gov.faa.notam.developerportal.model.entity.NotamApiToken;
import lombok.Data;

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
     * Enabled by the owner.
     */
    private Boolean isEnabledByUser;

    /**
     * Enabled by admin.
     */
    private Boolean isEnabledByAdmin;

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
    }
}
