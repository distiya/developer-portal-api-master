package gov.faa.notam.developerportal.model.api;

import lombok.Data;

import java.util.List;

@Data
public class RecentStatistics extends ModelSearchResult{

    /**
     * The items
     */
    private List<ApiRequestItemModel> items;

}
