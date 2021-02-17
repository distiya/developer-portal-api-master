package gov.faa.notam.developerportal.service;

import gov.faa.notam.developerportal.exception.ApiException;
import gov.faa.notam.developerportal.model.api.*;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

/**
 * API statics service.
 */
@Transactional
public interface StatisticsService {

    /**
     * This api is designed to save api access event
     *
     * @param createAccessEventModel The container object to hold the information about access event
     * @throws ApiException when an error
     */
    AccessEventModel saveAccessStatistics(CreateAccessEventModel createAccessEventModel) throws ApiException;

    /**
     * This api is designed to save api activity event
     *
     * @param itemType The type of the api access item
     * @param createActivityEventModel The container object to hold the information about activity event
     * @throws ApiException when an error
     */
    ActivityEventModel saveActivityStatistics(NotamApiAccessItemType itemType, CreateActivityEventModel createActivityEventModel) throws ApiException;

    /**
     * This api is designed to get the detail summary statistics
     *
     * @return The detail summary statistics
     * @throws ApiException when an error
     */
    StatisticsModel getSummaryStatistics() throws ApiException;

    /**
     * This api is designed get the geographical summary statistics
     *
     * @return The geographical summary statistics
     * @throws ApiException when an error
     */
    GeographyStatisticsModel getGeographyUsageStatistics() throws ApiException;

    /**
     * This api is designed to get usage statistics
     *
     * @param itemType The type of api access item
     * @param status The status of the token
     * @return Usage statistics based on token status
     * @throws ApiException when an error
     */
    ActiveUsagesModelSearchResult getStatistics(NotamApiAccessItemType itemType, NotamApiTokenStatus status) throws ApiException;


    Map<Long,LastTwentyFourHoursActivityModel> getLastTwentyFourHourActivityUsage(List<Long> userIDList) throws ApiException;

}
