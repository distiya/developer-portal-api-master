package gov.faa.notam.developerportal.model.api;

import lombok.Data;

@Data
public class ApiRequestsUsage {

    private Long last24hours;
    private Long last7days;
    private Long last30days;
    private Long lastyear;
    private RecentStatistics recentStatistics;
}
