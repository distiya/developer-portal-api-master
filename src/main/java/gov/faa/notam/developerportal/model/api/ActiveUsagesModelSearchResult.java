package gov.faa.notam.developerportal.model.api;

import lombok.Data;

import java.util.List;

@Data
public class ActiveUsagesModelSearchResult extends ModelSearchResult{

    /**
     * The items
     */
    private List<ActiveUsageItemModel> items;

}
