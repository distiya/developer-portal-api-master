package gov.faa.notam.developerportal.controller;

import gov.faa.notam.developerportal.exception.ApiException;
import gov.faa.notam.developerportal.model.api.*;
import gov.faa.notam.developerportal.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * The statistics endpoint.
 * <p>
 * POST /statistics/{itemType}/activity - Create activity
 * POST /statistics/access - Create access
 * </p>
 */
@RestController
@RequiredArgsConstructor
public class StatisticsController {
    /**
     * Statistics service.
     */
    private final StatisticsService statisticsService;

    @PostMapping(value = "/statistics/{itemType}/activity", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ActivityEventModel createActivity(@PathVariable("itemType") NotamApiAccessItemType itemType, @RequestBody CreateActivityEventModel request) throws ApiException {
        return statisticsService.saveActivityStatistics(itemType,request);
    }

    @PostMapping(value = "/statistics/access", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public AccessEventModel createAccess(@RequestBody CreateAccessEventModel request) throws ApiException {
        return statisticsService.saveAccessStatistics(request);
    }

    @GetMapping(value = "/statistics", produces = MediaType.APPLICATION_JSON_VALUE)
    public StatisticsModel getSummaryStatistics() throws ApiException {
        return statisticsService.getSummaryStatistics();
    }
}
