package gov.faa.notam.developerportal.model.api;

import lombok.Data;

import java.util.List;

@Data
public class UserRequestModelSearchResult extends ModelSearchResult{

    /**
     * The items
     */
    private List<UserRequestModel> items;

}
