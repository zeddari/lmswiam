package com.wiam.lms.repository.custom;

import com.wiam.lms.domain.Progression;
import com.wiam.lms.domain.custom.projection.interfaces.PeriodicReportPdfDetailInterface;
import com.wiam.lms.domain.custom.projection.interfaces.RowSeriesData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA repository for the Progression entity.
 */
@Repository
public interface DashboardRepository extends JpaRepository<Progression, Long> {
    @Query(
        value = "SELECT  day(start_time) xaxis, count(*) yaxis FROM progression where attendance in ('ABSENT_AUTHORIZED','ABSENT') \n" +
            "and MONTH(start_time) = MONTH(CURRENT_DATE())\n" +
            "AND YEAR(start_time) = YEAR(CURRENT_DATE())\n" +
            "group by day(start_time)",
        nativeQuery = true
    )
    List<RowSeriesData> getAbsenceRowSeries();

    @Query(
        value = "SELECT  count(*) yaxis FROM progression where attendance in ('ABSENT_AUTHORIZED','ABSENT') \n" +
            "and MONTH(start_time) = MONTH(CURRENT_DATE())\n" +
            "AND YEAR(start_time) = YEAR(CURRENT_DATE())\n" +
            "group by day(start_time)",
        nativeQuery = true
    )
    List<Long> getAbsenceRowCountPerDay();
    @Query(
        value = "SELECT  count(*) yaxis FROM progression where attendance in ('ABSENT_AUTHORIZED','ABSENT') \n" +
            "and MONTH(start_time) = MONTH(CURRENT_DATE())\n" +
            "AND YEAR(start_time) = YEAR(CURRENT_DATE())",
        nativeQuery = true
    )
    Long getAbsenceRowCount();

    @Query(
        value = "SELECT  count(*) yaxis FROM progression where attendance in ('ABSENT_AUTHORIZED','ABSENT') \n" +
            "and MONTH(start_time) = MONTH(CURRENT_DATE() - INTERVAL 1 MONTH)\n" +
            "AND YEAR(start_time) = YEAR(CURRENT_DATE())",
        nativeQuery = true
    )
    Long getAbsenceRowCountMonthBefore();
}
