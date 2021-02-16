package gov.faa.notam.developerportal.model.api;

import lombok.Data;

import java.util.List;

@Data
public class NotamApiAccessItemModelSearchResult extends ModelSearchResult{

    private List<NotamApiAccessItemModel> items;

}
