package com.wiam.lms.web.rest.custom;

import com.lowagie.text.DocumentException;
import com.wiam.lms.domain.Progression;
import com.wiam.lms.domain.SessionInstance;
import com.wiam.lms.domain.UserCustom;
import com.wiam.lms.domain.custom.projection.interfaces.Row3SeriesWithLabelData;
import com.wiam.lms.domain.custom.projection.interfaces.RowSeriesWithLabelData;
import com.wiam.lms.domain.dto.custom.ProgressionAllStudentQuery;
import com.wiam.lms.domain.dto.custom.ProgressionQuery;
import com.wiam.lms.domain.enumeration.Tilawa;
import com.wiam.lms.repository.ProgressionRepository;
import com.wiam.lms.repository.custom.DashboardRepository;
import com.wiam.lms.service.custom.dashboard.dto.AbsencesProfessorsYearData;
import com.wiam.lms.service.custom.dashboard.dto.ChartAdaeData;
import com.wiam.lms.service.custom.dashboard.dto.ChartAdaeSeries;
import com.wiam.lms.service.custom.dashboard.dto.ChartData;
import com.wiam.lms.service.custom.dashboard.dto.ChartSeries;
import com.wiam.lms.service.custom.dashboard.dto.LastAbsencesData;
import com.wiam.lms.service.custom.reporting.PdfService;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URISyntaxException;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
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
        Double deltaPercent = 0.d;
        try {
            if ((currentAbsence + lastMonthAbsence) != 0) deltaPercent =
                (double) (currentAbsence / (currentAbsence + lastMonthAbsence)) * 100;
        } catch (Exception e) {
            log.error("getAbsenceData : cannot calculate deltaPercent, error is : {}", e);
        }

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
            progressionQuery.getStudentId(),
            progressionQuery.getStartDate(),
            progressionQuery.getEndDate()
        );
        List<Integer> hifz = new ArrayList<>();
        List<Integer> tilawa = new ArrayList<>();
        List<Integer> moraja3a = new ArrayList<>();
        List<ZonedDateTime> dateXAxisTilawa = new ArrayList<>();
        List<ZonedDateTime> dateXAxisHifz = new ArrayList<>();
        List<ZonedDateTime> dateXAxisMoraja3a = new ArrayList<>();
        if (progressions != null) {
            progressions.forEach(progression -> {
                if (progression.getTilawaType().equals(Tilawa.HIFD)) {
                    hifz.add(progression.getHifdScore());
                    dateXAxisHifz.add(progression.getSessionInstance().getStartTime());
                }
                if (progression.getTilawaType().equals(Tilawa.TILAWA)) {
                    tilawa.add(progression.getTajweedScore());
                    dateXAxisTilawa.add(progression.getSessionInstance().getStartTime());
                }
                if (progression.getTilawaType().equals(Tilawa.MORAJA3A)) {
                    moraja3a.add(progression.getAdaeScore());
                    dateXAxisMoraja3a.add(progression.getSessionInstance().getStartTime());
                }
            });
        }
        ChartAdaeSeries chartHifdSeries = ChartAdaeSeries.builder().name("HIFD").data(hifz).date(dateXAxisHifz).build();
        ChartAdaeSeries chartMoraja3aSeries = ChartAdaeSeries.builder().name("MORAJA3A").data(moraja3a).date(dateXAxisMoraja3a).build();
        ChartAdaeSeries chartTilawaSeries = ChartAdaeSeries.builder().name("TILAWA").data(tilawa).date(dateXAxisTilawa).build();

        ChartAdaeData chartData = ChartAdaeData
            .builder()
            .chartTilawaSeries(chartTilawaSeries)
            .chartMoraja3aSeries(chartMoraja3aSeries)
            .chartHifdSeries(chartHifdSeries)
            .build();
        return ResponseEntity.ok(chartData);
    }

    @GetMapping("/adae/allStudent/data")
    public ResponseEntity<ChartAdaeData> getAllAdaeData(@RequestBody ProgressionAllStudentQuery progressionQuery)
        throws URISyntaxException, IOException, DocumentException {
        log.debug("REST request to getAbsenceData  for all Student");
        List<Progression> progressions = progressionRepository.findAllStudentIdAndSessionInstanceBetween(
            progressionQuery.getSessionInstanceId(),
            Date.from(progressionQuery.getStartDate().atStartOfDay(ZoneId.systemDefault()).toInstant()),
            Date.from(progressionQuery.getEndDate().atStartOfDay(ZoneId.systemDefault()).toInstant())
        );
        List<Integer> hifz = new ArrayList<>();
        List<Integer> tilawa = new ArrayList<>();
        List<Integer> moraja3a = new ArrayList<>();
        List<ZonedDateTime> dateXAxisTilawa = new ArrayList<>();
        List<ZonedDateTime> dateXAxisHifz = new ArrayList<>();
        List<ZonedDateTime> dateXAxisMoraja3a = new ArrayList<>();
        if (progressions != null) {
            progressions.forEach(progression -> {
                if (progression.getTilawaType().equals(Tilawa.HIFD)) {
                    hifz.add(progression.getHifdScore());
                    dateXAxisHifz.add(progression.getSessionInstance().getStartTime());
                }
                if (progression.getTilawaType().equals(Tilawa.TILAWA)) {
                    tilawa.add(progression.getTajweedScore());
                    dateXAxisTilawa.add(progression.getSessionInstance().getStartTime());
                }
                if (progression.getTilawaType().equals(Tilawa.MORAJA3A)) {
                    moraja3a.add(progression.getAdaeScore());
                    dateXAxisMoraja3a.add(progression.getSessionInstance().getStartTime());
                }
            });
        }
        ChartAdaeSeries chartHifdSeries = ChartAdaeSeries.builder().name("HIFD").data(hifz).date(dateXAxisHifz).build();
        ChartAdaeSeries chartMoraja3aSeries = ChartAdaeSeries.builder().name("MORAJA3A").data(moraja3a).date(dateXAxisMoraja3a).build();
        ChartAdaeSeries chartTilawaSeries = ChartAdaeSeries.builder().name("TILAWA").data(tilawa).date(dateXAxisTilawa).build();

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
        return progressionRepository.getStudentAbsenceDataDate(
            progressionQuery.getStudentId(),
            Date.from(progressionQuery.getStartDate().atStartOfDay(ZoneId.systemDefault()).toInstant()),
            Date.from(progressionQuery.getEndDate().atStartOfDay(ZoneId.systemDefault()).toInstant())
        );
    }

    @PostMapping("/absence/allStudent/data")
    public List<Row3SeriesWithLabelData> getAbsenceAllStudentData(@RequestBody ProgressionAllStudentQuery progressionQuery)
        throws URISyntaxException, IOException, DocumentException {
        log.debug("REST request to getAbsenceData for all Student");
        return progressionRepository.getAllStudentAbsenceDataDate(
            progressionQuery.getSessionInstanceId(),
            Date.from(progressionQuery.getStartDate().atStartOfDay(ZoneId.systemDefault()).toInstant()),
            Date.from(progressionQuery.getEndDate().atStartOfDay(ZoneId.systemDefault()).toInstant())
        );
    }

    @GetMapping("/absence/professors/lastabsences")
    public List<LastAbsencesData> getLastAbsencesProfessorData() throws URISyntaxException, IOException, DocumentException {
        log.debug("REST request to getLastAbsencesProfessorData");
        List<SessionInstance> lastSessionAbsences = dashboardRepository.getLastSessionInstancesWithAbsences(PageRequest.of(0, 10));
        //        log.debug("count number of absences "+lastSessionAbsences.size());
        List<LastAbsencesData> result = new ArrayList<LastAbsencesData>();
        lastSessionAbsences.forEach(sessionInstance -> {
            UserCustom prof = sessionInstance.getSession1().getProfessors().iterator().next();
            String photoContentType = "image/jpg";
            String avatarBase64 =
                "data:" +
                photoContentType +
                ";base64," +
                "/9j/4QAYRXhpZgAASUkqAAgAAAAAAAAAAAAAAP/sABFEdWNreQABAAQAAAAyAAD/4QMuaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wLwA8P3hwYWNrZXQgYmVnaW49Iu+7vyIgaWQ9Ilc1TTBNcENlaGlIenJlU3pOVGN6a2M5ZCI/PiA8eDp4bXBtZXRhIHhtbG5zOng9ImFkb2JlOm5zOm1ldGEvIiB4OnhtcHRrPSJBZG9iZSBYTVAgQ29yZSA2LjAtYzAwNiA3OS4xNjQ2NDgsIDIwMjEvMDEvMTItMTU6NTI6MjkgICAgICAgICI+IDxyZGY6UkRGIHhtbG5zOnJkZj0iaHR0cDovL3d3dy53My5vcmcvMTk5OS8wMi8yMi1yZGYtc3ludGF4LW5zIyI+IDxyZGY6RGVzY3JpcHRpb24gcmRmOmFib3V0PSIiIHhtbG5zOnhtcD0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wLyIgeG1sbnM6eG1wTU09Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9tbS8iIHhtbG5zOnN0UmVmPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvc1R5cGUvUmVzb3VyY2VSZWYjIiB4bXA6Q3JlYXRvclRvb2w9IkFkb2JlIFBob3Rvc2hvcCAyMi4yIChNYWNpbnRvc2gpIiB4bXBNTTpJbnN0YW5jZUlEPSJ4bXAuaWlkOkI4QzdGM0Y5ODcyRTExRUNBRUQyRDhCQjY5RUVFNkIyIiB4bXBNTTpEb2N1bWVudElEPSJ4bXAuZGlkOkI4QzdGM0ZBODcyRTExRUNBRUQyRDhCQjY5RUVFNkIyIj4gPHhtcE1NOkRlcml2ZWRGcm9tIHN0UmVmOmluc3RhbmNlSUQ9InhtcC5paWQ6QjhDN0YzRjc4NzJFMTFFQ0FFRDJEOEJCNjlFRUU2QjIiIHN0UmVmOmRvY3VtZW50SUQ9InhtcC5kaWQ6QjhDN0YzRjg4NzJFMTFFQ0FFRDJEOEJCNjlFRUU2QjIiLz4gPC9yZGY6RGVzY3JpcHRpb24+IDwvcmRmOlJERj4gPC94OnhtcG1ldGE+IDw/eHBhY2tldCBlbmQ9InIiPz7/7gAOQWRvYmUAZMAAAAAB/9sAhAAIBgYGBgYIBgYIDAgHCAwO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";
            if (sessionInstance.getProfessor() != null) {
                photoContentType = sessionInstance.getProfessor().getPhotoContentType();
                avatarBase64 =
                    "data:" +
                    photoContentType +
                    ";base64," +
                    Base64.getEncoder().encodeToString((sessionInstance.getProfessor().getPhoto()));
            }
            result.add(
                LastAbsencesData
                    .builder()
                    .id(sessionInstance.getId())
                    .name(prof.getFirstName())
                    .title(sessionInstance.getSession1().getTitle())
                    .date(sessionInstance.getStartTime())
                    .photoBase64(avatarBase64)
                    .build()
            );
        });
        return result;
    }

    @GetMapping("/absence/professors/year")
    public AbsencesProfessorsYearData getAbsencesProfessorYearData() throws URISyntaxException, IOException, DocumentException {
        log.debug("REST request to getAbsencesProfessorYearData");
        AbsencesProfessorsYearData absencesProfessorsYearData = AbsencesProfessorsYearData
            .builder()
            .chartSeries(ChartSeries.builder().data(new ArrayList<Long>()).build())
            .months(new ArrayList<String>())
            .build();
        for (int i = 11; i >= 0; i--) {
            YearMonth lastMonth = YearMonth.now().minusMonths(i);
            Integer countAbsencesMonth = dashboardRepository.countAbsencesMonth(lastMonth.toString());
            absencesProfessorsYearData.getChartSeries().getData().add((long) countAbsencesMonth);
            absencesProfessorsYearData.getMonths().add(lastMonth.toString());
        }
        return absencesProfessorsYearData;
    }
}
