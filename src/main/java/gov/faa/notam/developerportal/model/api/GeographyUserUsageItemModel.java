package gov.faa.notam.developerportal.model.api;

import lombok.Data;

@Data
public class GeographyUserUsageItemModel {

    /**
     * Country name
     */
    private String country;

    /**
     * Total usage in country
     */
    private Long usageCount;

    /**
     * Error activity
     */
    private Long rate;

}
