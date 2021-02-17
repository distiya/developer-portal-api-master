package gov.faa.notam.developerportal.service.impl;

import gov.faa.notam.developerportal.exception.ApiException;
import gov.faa.notam.developerportal.model.api.*;
import gov.faa.notam.developerportal.model.entity.*;
import gov.faa.notam.developerportal.repository.*;
import gov.faa.notam.developerportal.security.SecurityUtil;
import gov.faa.notam.developerportal.service.StatisticsService;
import gov.faa.notam.developerportal.service.ValidationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatisticsServiceImpl implements StatisticsService {

    private final ValidationService validationService;
    private final UserRepository userRepository;
    private final AccessRepository accessRepository;
    private  final ActivityRepository activityRepository;
    private final UserCountRepository userCountRepository;
    private final HourlyAPIUsageRepository hourlyAPIUsageRepository;
    private final NotamApiTokenRepository notamApiTokenRepository;

    @Override
    public AccessEventModel saveAccessStatistics(CreateAccessEventModel createAccessEventModel) throws ApiException {
        validationService.validateAdminAccessRight();
        Access access = new Access();
        access.setStatus(ApiAccessStatus.successes);
        access.setSource(createAccessEventModel.getSource());
        access.setDateAndTime(Date.from(Instant.now()));
        access = accessRepository.save(access);
        return new AccessEventModel(access);
    }

    @Override
    public ActivityEventModel saveActivityStatistics(NotamApiAccessItemType itemType, CreateActivityEventModel createActivityEventModel) throws ApiException {
        validationService.validateAdminAccessRight();
        Long userId = SecurityUtil.getCurrentUserId().get();
        Activity activity = new Activity();
        activity.setUserID(userId);
        activity.setName(createActivityEventModel.getName());
        Date createdDate = Date.from(Instant.now());
        setDateTimeHour(createdDate);
        activity.setDateTimeHour(createdDate);
        activity.setType(itemType);
        activity.setStatus(ApiActivityStatus.active);
        activity = activityRepository.save(activity);
        return new ActivityEventModel(activity);
    }

    @Override
    public StatisticsModel getSummaryStatistics() throws ApiException {
        validationService.validateAdminAccessRight();
        long totalUserCount = userRepository.count();
        List<UserCountPerCountryModel> userCountPerCountry = userCountRepository.findAll().stream().map(UserCountPerCountryModel::new).collect(Collectors.toList());
        List<HourlyApiUsageModel> hourlyAPIUsageCount = hourlyAPIUsageRepository.findAll().stream().map(HourlyApiUsageModel::new).collect(Collectors.toList());
        List<ActivityStatistics> activityStatistics = activityRepository.countActivityByStatus();
        List<AccessStatistics> accessStatistics = accessRepository.countAccessByStatus();
        StatisticsModel statisticsModel = new StatisticsModel();
        statisticsModel.setTotalUserCount((int)totalUserCount);
        statisticsModel.setUserCountPerCountry(userCountPerCountry);
        statisticsModel.setHourlyApiUsage(hourlyAPIUsageCount);
        statisticsModel.setApiActivity(getApiActivityModel(activityStatistics));
        statisticsModel.setAccesses(getApiAccessModel(accessStatistics));
        List<ActiveUsageItemModel> accessKeys = notamApiTokenRepository.findAll().stream().map(t -> {
            ActiveUsageItemModel activeUsageItemModel = new ActiveUsageItemModel();
            activeUsageItemModel.setAccessKey(t.getKey());
            activeUsageItemModel.setRequestor(t.getUser().getFullName());
            activeUsageItemModel.setCreatedDate(t.getCreatedAt());
            return activeUsageItemModel;
        })
        .collect(Collectors.toList());
        statisticsModel.setAccesskeys(accessKeys);
        return statisticsModel;
    }

    @Override
    public GeographyStatisticsModel getGeographyUsageStatistics() throws ApiException {
        validationService.validateAdminAccessRight();
        return null;
    }

    @Override
    public ActiveUsagesModelSearchResult getStatistics(NotamApiAccessItemType itemType, NotamApiTokenStatus status) throws ApiException {
        validationService.validateAdminAccessRight();
        return null;
    }

    @Override
    public Map<Long,LastTwentyFourHoursActivityModel> getLastTwentyFourHourActivityUsage(List<Long> userIDList) throws ApiException {
        Date endDateTimeHour = Date.from(Instant.now());
        Date startDateTimeHour = Date.from(Instant.now());
        setDateTimeHour(endDateTimeHour);
        setDateTimeHour(startDateTimeHour);
        startDateTimeHour.setTime(startDateTimeHour.getTime() - 86400000L);
        List<UserActivityStatistics> userActivityStatistics = activityRepository.countActivityByStatusForUsersWithTimeBound(userIDList, startDateTimeHour, endDateTimeHour);
        Map<Long,LastTwentyFourHoursActivityModel> staticMap = new HashMap<>();
        userActivityStatistics.stream().forEach(us->{
            LastTwentyFourHoursActivityModel statistic = staticMap.get(us.getUserID());
            if(statistic == null){
                statistic = new LastTwentyFourHoursActivityModel();
                staticMap.put(us.getUserID(),statistic);
            }
            decorateLastTwentyFourHourActivityModel(statistic,us);
        });
        return staticMap;
    }

    private void setDateTimeHour(Date date){
        long divisionFactor = 3600000L;
        date.setTime((date.getTime()/divisionFactor)*divisionFactor);
    }

    private void decorateLastTwentyFourHourActivityModel(LastTwentyFourHoursActivityModel ls, UserActivityStatistics as){
        switch (as.getStatus()){
            case active: ls.setSuccess(as.getCount());
            break;
            case errors: ls.setError(as.getCount());
            break;
        }
    }

    private List<ApiActivityModel> getApiActivityModel(List<ActivityStatistics> activityStatistics){
        ApiActivityModel apiActivityModel = new ApiActivityModel();
        activityStatistics.stream().forEach(s->{
            switch (s.getStatus()){
                case active: apiActivityModel.setActive(s.getCount());
                break;
                case inactive: apiActivityModel.setInactive(s.getCount());
                break;
                case requests: apiActivityModel.setRequests(s.getCount());
                break;
                case errors: apiActivityModel.setErrors(s.getCount());
                break;
            }
        });
        List<ApiActivityModel> apiActivityModels = new ArrayList<>();
        apiActivityModels.add(apiActivityModel);
        return apiActivityModels;
    }

    private List<AccessesModel> getApiAccessModel(List<AccessStatistics> accessStatistics){
        AccessesModel accessesModel = new AccessesModel();
        accessStatistics.stream().forEach(s->{
            switch (s.getStatus()){
                case successes: accessesModel.setSuccesses(s.getCount());
                break;
                case requests: accessesModel.setRequests(s.getCount());
                break;
                case errors904c: accessesModel.setErrors904c(s.getCount());
                break;
                case othererrors: accessesModel.setOthererrors(s.getCount());
                break;
            }
        });
        List<AccessesModel> apiAccessModels = new ArrayList<>();
        apiAccessModels.add(accessesModel);
        return apiAccessModels;
    }


}
