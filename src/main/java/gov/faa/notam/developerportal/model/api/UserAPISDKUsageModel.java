package gov.faa.notam.developerportal.model.api;

import lombok.Data;

@Data
public class UserAPISDKUsageModel {

    /**
     * UserID
     */
    private Long id;

    /**
     * The total APIs subscribed by user
     */
    private Long apisSubscribed;

    /**
     * The total pending requests of user
     */
    private Long pendingRequests;

}
