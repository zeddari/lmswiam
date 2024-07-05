package com.wiam.lms.repository.custom;

import com.wiam.lms.domain.Progression;
import com.wiam.lms.domain.custom.projection.interfaces.PeriodicReportPdfDetailInterface;
import com.wiam.lms.domain.custom.projection.interfaces.RowSeriesData;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Progression entity.
 */
@Repository
public interface DashboardRepository extends JpaRepository<Progression, Long> {
    @Query(
        value = "SELECT  day(start_time) xaxis, count(*) yaxis FROM progression where attendance in ('ABSENT_AUTHORIZED','ABSENT') " +
        "and MONTH(start_time) = MONTH(CURRENT_DATE()) " +
        "AND YEAR(start_time) = YEAR(CURRENT_DATE()) " +
        "group by day(start_time)",
        nativeQuery = true
    )
    List<RowSeriesData> getAbsenceRowSeries();

    @Query(
        value = "SELECT  count(*) yaxis FROM progression where attendance in ('ABSENT_AUTHORIZED','ABSENT') " +
        "and MONTH(start_time) = MONTH(CURRENT_DATE()) " +
        "AND YEAR(start_time) = YEAR(CURRENT_DATE()) " +
        "group by day(start_time)",
        nativeQuery = true
    )
    List<Long> getAbsenceRowCountPerDay();

    @Query(
        value = "SELECT  count(*) yaxis FROM progression where attendance in ('ABSENT_AUTHORIZED','ABSENT') " +
        "and MONTH(start_time) = MONTH(CURRENT_DATE()) " +
        "AND YEAR(start_time) = YEAR(CURRENT_DATE())",
        nativeQuery = true
    )
    Long getAbsenceRowCount();

    @Query(
        value = "SELECT  count(*) yaxis FROM progression where attendance in ('ABSENT_AUTHORIZED','ABSENT') " +
        "and MONTH(start_time) = MONTH(CURRENT_DATE() - INTERVAL 1 MONTH) " +
        "AND YEAR(start_time) = YEAR(CURRENT_DATE())",
        nativeQuery = true
    )
    Long getAbsenceRowCountMonthBefore();

    @Query(
        value = "select COALESCE(floor(sum(amount)),0) yaxis from payment where side in ('IN')" +
        " AND MONTH(paid_at) = MONTH(CURRENT_DATE())" +
        " AND YEAR(paid_at) = YEAR(CURRENT_DATE())",
        nativeQuery = true
    )
    Long getIncomeCurrentMonth();

    @Query(
        value = "select COALESCE(floor(sum(amount)),0) yaxis from payment where side in ('IN')" +
        " AND MONTH(paid_at) = MONTH(CURRENT_DATE() - INTERVAL 1 MONTH)" +
        " AND YEAR(paid_at) = YEAR(CURRENT_DATE())",
        nativeQuery = true
    )
    Long getIncomeLastMonth();

    @Query(
        value = "select floor(sum(amount)) yaxis from payment where side in ('IN')" +
        " AND MONTH(paid_at) = MONTH(CURRENT_DATE())" +
        " AND YEAR(paid_at) = YEAR(CURRENT_DATE())" +
        " group by day(paid_at)",
        nativeQuery = true
    )
    List<Long> getIncomeListPerDay();

    @Query(
        value = "select COALESCE(floor(sum(amount)),0) yaxis from payment where side in ('OUT')" +
        " AND MONTH(paid_at) = MONTH(CURRENT_DATE())" +
        " AND YEAR(paid_at) = YEAR(CURRENT_DATE())",
        nativeQuery = true
    )
    Long getExpensesCurrentMonth();

    @Query(
        value = "select COALESCE(floor(sum(amount)),0) yaxis from payment where side in ('OUT')" +
        " AND MONTH(paid_at) = MONTH(CURRENT_DATE() - INTERVAL 1 MONTH)" +
        " AND YEAR(paid_at) = YEAR(CURRENT_DATE())",
        nativeQuery = true
    )
    Long getExpensesLastMonth();

    @Query(
        value = "select floor(sum(amount)) yaxis from payment where side in ('OUT')" +
        " AND MONTH(paid_at) = MONTH(CURRENT_DATE())" +
        " AND YEAR(paid_at) = YEAR(CURRENT_DATE())" +
        " group by day(paid_at)",
        nativeQuery = true
    )
    List<Long> getExpensesListPerDay();

    @Query(
        value = "select COALESCE(floor(sum(amount)),0) yaxis from sponsoring where " +
        "CURRENT_DATE()>=CAST(DATE_FORMAT(start_date ,'%Y-%m-01') as DATE) " +
        "And CAST(DATE_FORMAT(CURRENT_DATE() ,'%Y-%m-01') as DATE) <=end_date AND is_always",
        nativeQuery = true
    )
    Long getSponsorshipCurrentMonth();

    @Query(
        value = "select COALESCE(floor(sum(amount)),0) yaxis from sponsoring where " +
        "(CURRENT_DATE() - INTERVAL 1 MONTH)>=CAST(DATE_FORMAT(start_date ,'%Y-%m-01') as DATE) " +
        "And CAST(DATE_FORMAT((CURRENT_DATE() - INTERVAL 1 MONTH) ,'%Y-%m-01') as DATE) <=end_date AND is_always",
        nativeQuery = true
    )
    Long getSponsorshipLastMonth();

    @Query(
        value = "select floor(amount) yaxis from sponsoring where " +
        "CURRENT_DATE()>=CAST(DATE_FORMAT(start_date ,'%Y-%m-01') as DATE) " +
        "And CAST(DATE_FORMAT(CURRENT_DATE() ,'%Y-%m-01') as DATE) <=end_date AND is_always",
        nativeQuery = true
    )
    List<Long> getSponsorshipList();
}
