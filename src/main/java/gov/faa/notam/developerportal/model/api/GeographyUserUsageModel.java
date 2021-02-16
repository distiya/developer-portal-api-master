package gov.faa.notam.developerportal.model.api;

import lombok.Data;

import java.util.List;

@Data
public class GeographyUserUsageModel extends ModelSearchResult{

    /**
     * The items
     */
    private List<GeographyUserUsageItemModel> items;

}
