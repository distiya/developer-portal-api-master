package gov.faa.notam.developerportal.model.api;

import lombok.Data;

@Data
public class UpdateNotamApiAccessItemModel {

    /**
     * The version
     */
    private String version;

    /**
     * The description
     */
    private String description;

    /**
     * The change log
     */
    private String changeLog;


}
