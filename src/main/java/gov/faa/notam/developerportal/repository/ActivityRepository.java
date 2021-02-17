package gov.faa.notam.developerportal.repository;

import gov.faa.notam.developerportal.model.entity.Activity;
import gov.faa.notam.developerportal.model.entity.ActivityStatistics;
import gov.faa.notam.developerportal.model.entity.UserActivityStatistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface ActivityRepository extends JpaRepository<Activity, Long>, JpaSpecificationExecutor<Activity> {

    @Query("select new gov.faa.notam.developerportal.model.entity.ActivityStatistics(u.status,count(u.id) as statusGroupCount) from Activity u group by u.status")
    List<ActivityStatistics> countActivityByStatus();

    @Query("select new gov.faa.notam.developerportal.model.entity.UserActivityStatistics(u.userID,u.status,count(u.id) as statusGroupCount) from Activity u where u.userID in :userIDList and u.dateTimeHour >= :startDateTimeHour and u.dateTimeHour <= :endDateTimeHour group by u.userID,u.status")
    List<UserActivityStatistics> countActivityByStatusForUsersWithTimeBound(List<Long> userIDList, Date startDateTimeHour, Date endDateTimeHour);

}
