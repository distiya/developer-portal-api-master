package gov.faa.notam.developerportal.model.api;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class CreateNotamAPIAccessItemRequest {

    @NotNull(message = "Item type is mandatory")
    private NotamApiAccessItemType itemType;

    @NotBlank(message = "Version should not be blank")
    private String version;

    @NotBlank(message = "Description should not be blank")
    private String description;

    @NotBlank(message = "Change log should not be blank")
    private String changeLog;

}
