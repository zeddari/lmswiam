package com.wiam.lms.web.rest.custom;

import com.lowagie.text.DocumentException;
import com.lowagie.text.DocumentException;
import com.wiam.lms.domain.Progression;
import com.wiam.lms.domain.custom.projection.interfaces.PeriodicReportPdfDetailInterface;
import com.wiam.lms.domain.custom.projection.interfaces.RowSeriesData;
import com.wiam.lms.domain.custom.projection.interfaces.RowSeriesWithLabelData;
import com.wiam.lms.domain.dto.custom.ProgressionQuery;
import com.wiam.lms.repository.ProgressionRepository;
import com.wiam.lms.repository.custom.DashboardRepository;
import com.wiam.lms.repository.custom.DashboardRepository;
import com.wiam.lms.repository.custom.ReportingRepository;
import com.wiam.lms.service.custom.dashboard.dto.ChartAdaeData;
import com.wiam.lms.service.custom.dashboard.dto.ChartAdaeSeries;
import com.wiam.lms.service.custom.dashboard.dto.ChartData;
import com.wiam.lms.service.custom.dashboard.dto.ChartData;
import com.wiam.lms.service.custom.dashboard.dto.ChartSeries;
import com.wiam.lms.service.custom.dashboard.dto.ChartSeries;
import com.wiam.lms.service.custom.reporting.PdfService;
import com.wiam.lms.service.custom.reporting.PdfService;
import com.wiam.lms.service.custom.reporting.request.PeriodicReportPdfRequest;
import jakarta.validation.Valid;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URISyntaxException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @Autowired
    private ProgressionRepository progressionRepository;

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
        Double deltaPercent = extractDeltaPercent(currentIncome, lastMonthIncome);
        ChartSeries chartSeries = ChartSeries.builder().data(incomePerDay).build();
        ChartData chartData = ChartData
            .builder()
            .chartSeries(chartSeries)
            .countItems(currentIncome)
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
    @GetMapping("/expenses/data")
    public ResponseEntity<ChartData> getExpensesData() throws URISyntaxException, IOException, DocumentException {
        log.debug("REST request to getExpensesData");
        List<Long> expensesPerDay = dashboardRepository.getExpensesListPerDay();
        Long currentExpenses = dashboardRepository.getExpensesCurrentMonth();
        Long lastMonthExpenses = dashboardRepository.getExpensesLastMonth();
        Double deltaPercent = extractDeltaPercent(currentExpenses, lastMonthExpenses);
        ChartSeries chartSeries = ChartSeries.builder().data(expensesPerDay).build();
        ChartData chartData = ChartData
            .builder()
            .chartSeries(chartSeries)
            .countItems(currentExpenses)
            .deltaLastMonthPercent(deltaPercent)
            .build();
        return ResponseEntity.ok(chartData);
    }

    private Double extractDeltaPercent(Long currentExpenses, Long lastMonthExpenses) {
        Double deltaPercent = (lastMonthExpenses != 0)
            ? (((double) currentExpenses - lastMonthExpenses) / (lastMonthExpenses)) * 100
            : null;
        return percentPrecision(deltaPercent);
    }

    /**
     *
     * @return
     * @throws URISyntaxException
     * @throws IOException
     * @throws DocumentException
     */
    @GetMapping("/sponsorship/data")
    public ResponseEntity<ChartData> getSponsorshipData() throws URISyntaxException, IOException, DocumentException {
        log.debug("REST request to getSponsorshipData");
        List<Long> sponsorships = dashboardRepository.getSponsorshipList();
        Long currentSponsorship = dashboardRepository.getSponsorshipCurrentMonth();
        Long lastMonthSponsorship = dashboardRepository.getSponsorshipLastMonth();
        Double deltaPercent = extractDeltaPercent(currentSponsorship, lastMonthSponsorship);
        ChartSeries chartSeries = ChartSeries.builder().data(sponsorships).build();
        ChartData chartData = ChartData
            .builder()
            .chartSeries(chartSeries)
            .countItems(currentSponsorship)
            .deltaLastMonthPercent(deltaPercent)
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

    @GetMapping("/adae/student/data")
    public ResponseEntity<ChartAdaeData> getAdaeData(@RequestBody ProgressionQuery progressionQuery)
        throws URISyntaxException, IOException, DocumentException {
        log.debug("REST request to getAbsenceData");
        List<Progression> progressions = progressionRepository.findAllByStudentIdAndSessionInstanceBetween(
            progressionQuery.getSiteId(),
            progressionQuery.getStudentId(),
            progressionQuery.getStartDate(),
            progressionQuery.getEndDate()
        );
        List<Integer> hifz = new ArrayList<>();
        List<Integer> tilawa = new ArrayList<>();
        List<Integer> moraja3a = new ArrayList<>();
        List<ZonedDateTime> dateXAxis = new ArrayList<>();
        if (progressions != null) {
            progressions.forEach(progression -> {
                if (progression.getTilawaType().equals("HIFD")) hifz.add(progression.getHifdScore());
                if (progression.getTilawaType().equals("TILAWA")) tilawa.add(progression.getTajweedScore());
                if (progression.getTilawaType().equals("MORAJA3A")) moraja3a.add(progression.getAdaeScore());
                dateXAxis.add(progression.getSessionInstance().getStartTime());
            });
        }
        ChartAdaeSeries chartHifdSeries = ChartAdaeSeries.builder().name("HIFD").data(hifz).date(dateXAxis).build();
        ChartAdaeSeries chartMoraja3aSeries = ChartAdaeSeries.builder().name("MORAJA3A").data(moraja3a).date(dateXAxis).build();
        ChartAdaeSeries chartTilawaSeries = ChartAdaeSeries.builder().name("TILAWA").data(tilawa).date(dateXAxis).build();

        ChartAdaeData chartData = ChartAdaeData
            .builder()
            .chartTilawaSeries(chartTilawaSeries)
            .chartMoraja3aSeries(chartMoraja3aSeries)
            .chartHifdSeries(chartHifdSeries)
            .build();
        return ResponseEntity.ok(chartData);
    }

    @GetMapping("/absence/student/data")
    public List<RowSeriesWithLabelData> getAbsenceStudentData(@RequestBody ProgressionQuery progressionQuery)
        throws URISyntaxException, IOException, DocumentException {
        log.debug("REST request to getAbsenceData");
        return progressionRepository.getStudentAbsenceData(
            progressionQuery.getStudentId(),
            progressionQuery.getStartDate(),
            progressionQuery.getEndDate()
        );
    }
}
