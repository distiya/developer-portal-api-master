package gov.faa.notam.developerportal.model.api;

import lombok.Data;

import java.util.List;

@Data
public class StatisticsModel {

    private Integer totalUserCount;
    private List<UserCountPerCountryModel> userCountPerCountry;
    private List<HourlyApiUsageModel> hourlyApiUsage;
    private List<ApiActivityModel> apiActivity;
    private List<AccessesModel> accesses;
    private List<ActiveUsageItemModel> accesskeys;

}
