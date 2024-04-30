package com.wiam.lms.web.rest.custom;

import com.lowagie.text.DocumentException;
import com.wiam.lms.domain.custom.projection.interfaces.PeriodicReportPdfDetailInterface;
import com.wiam.lms.domain.custom.projection.interfaces.RowSeriesData;
import com.wiam.lms.repository.custom.DashboardRepository;
import com.wiam.lms.repository.custom.ReportingRepository;
import com.wiam.lms.service.custom.dashboard.dto.ChartData;
import com.wiam.lms.service.custom.dashboard.dto.ChartSeries;
import com.wiam.lms.service.custom.reporting.PdfService;
import com.wiam.lms.service.custom.reporting.request.PeriodicReportPdfRequest;
import jakarta.validation.Valid;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URISyntaxException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for managing {Dashboard}.
 */
@RestController
@RequestMapping("/api/dashboard")
@Transactional
public class DashboardResource {

    private final Logger log = LoggerFactory.getLogger(DashboardResource.class);

    private static final String ENTITY_NAME = "NA";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    @Autowired
    private PdfService pdfService;

    @Autowired
    private DashboardRepository dashboardRepository;

    /**
     *
     * @return
     * @throws URISyntaxException
     * @throws IOException
     * @throws DocumentException
     */
    @GetMapping("/absence/data")
    public ResponseEntity<ChartData> getAbsenceData() throws URISyntaxException, IOException, DocumentException {
        log.debug("REST request to getAbsenceData");
        List<Long> rowCountPerDay = dashboardRepository.getAbsenceRowCountPerDay();
        Long currentAbsence = dashboardRepository.getAbsenceRowCount();
        Long lastMonthAbsence = dashboardRepository.getAbsenceRowCountMonthBefore();
        Double deltaPercent = (double) (currentAbsence / (currentAbsence + lastMonthAbsence)) * 100;
        ChartSeries chartSeries = ChartSeries.builder().data(rowCountPerDay).build();
        ChartData chartData = ChartData
            .builder()
            .chartSeries(chartSeries)
            .countItems(currentAbsence)
            .deltaLastMonthPercent(deltaPercent)
            .build();
        return ResponseEntity.ok(chartData);
    }

    /**
     *
     * @return
     * @throws URISyntaxException
     * @throws IOException
     * @throws DocumentException
     */
    @GetMapping("/income/data")
    public ResponseEntity<ChartData> getIncomeData() throws URISyntaxException, IOException, DocumentException {
        log.debug("REST request to getIncomeData");
        List<Long> incomePerDay = dashboardRepository.getIncomeListPerDay();
        Long currentIncome = dashboardRepository.getIncomeCurrentMonth();
        Long lastMonthIncome = dashboardRepository.getIncomeLastMonth();
        Double deltaPercent = (lastMonthIncome != 0) ? (((double) currentIncome - lastMonthIncome) / (lastMonthIncome)) * 100 : null;
        ChartSeries chartSeries = ChartSeries.builder().data(incomePerDay).build();
        ChartData chartData = ChartData
            .builder()
            .chartSeries(chartSeries)
            .countItems(currentIncome)
            .deltaLastMonthPercent(percentPrecision(deltaPercent))
            .build();
        return ResponseEntity.ok(chartData);
    }

    /**
     *
     * @return
     * @throws URISyntaxException
     * @throws IOException
     * @throws DocumentException
     */
    @GetMapping("/expenses/data")
    public ResponseEntity<ChartData> getExpensesData() throws URISyntaxException, IOException, DocumentException {
        log.debug("REST request to getExpensesData");
        List<Long> expensesPerDay = dashboardRepository.getExpensesListPerDay();
        Long currentExpenses = dashboardRepository.getExpensesCurrentMonth();
        Long lastMonthExpenses = dashboardRepository.getExpensesLastMonth();
        Double deltaPercent = (lastMonthExpenses != 0)
            ? (((double) currentExpenses - lastMonthExpenses) / (lastMonthExpenses)) * 100
            : null;
        ChartSeries chartSeries = ChartSeries.builder().data(expensesPerDay).build();
        ChartData chartData = ChartData
            .builder()
            .chartSeries(chartSeries)
            .countItems(currentExpenses)
            .deltaLastMonthPercent(percentPrecision(deltaPercent))
            .build();
        return ResponseEntity.ok(chartData);
    }

    private Double percentPrecision(Double val) {
        if (val != null) {
            return new BigDecimal(val.toString()).setScale(2, RoundingMode.HALF_DOWN).doubleValue();
        } else {
            return null;
        }
    }
}
