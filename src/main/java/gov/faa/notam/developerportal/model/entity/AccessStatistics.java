package gov.faa.notam.developerportal.model.entity;

import gov.faa.notam.developerportal.model.api.ApiAccessStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccessStatistics {

    private ApiAccessStatus status;
    private Long count;

}
