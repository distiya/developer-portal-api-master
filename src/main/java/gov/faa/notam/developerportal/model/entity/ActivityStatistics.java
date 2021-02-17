package gov.faa.notam.developerportal.model.entity;

import gov.faa.notam.developerportal.model.api.ApiActivityStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActivityStatistics {

    private ApiActivityStatus status;
    private Long count;

}
