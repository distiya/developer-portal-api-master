package gov.faa.notam.developerportal.model.api;

import lombok.Data;

import java.util.List;

@Data
public class GeographyStatisticsModel {

    private List<GeographyUserUsageModel> usersUsage;
    private ApiRequestsUsage apiRequestsUsage;
    private RecentlyUsedApiUsage recentlyUsedApiUsage;

}
