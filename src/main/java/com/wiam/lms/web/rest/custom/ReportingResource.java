package com.wiam.lms.web.rest.custom;

import com.lowagie.text.DocumentException;
import com.wiam.lms.domain.Progression;
import com.wiam.lms.domain.custom.projection.dto.PeriodicReportPdfDetail;
import com.wiam.lms.domain.custom.projection.interfaces.PeriodicReportPdfDetailInterface;
import com.wiam.lms.repository.ProgressionRepository;
import com.wiam.lms.repository.customer.ReportingRepository;
import com.wiam.lms.service.reporting.PdfService;
import com.wiam.lms.service.reporting.request.PeriodicReportPdfRequest;
import jakarta.validation.Valid;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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
 * REST controller for managing {Reporting}.
 */
@RestController
@RequestMapping("/api/reporting")
@Transactional
public class ReportingResource {

    private final Logger log = LoggerFactory.getLogger(ReportingResource.class);

    private static final String ENTITY_NAME = "NA";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    @Autowired
    private PdfService pdfService;

    @Autowired
    private ReportingRepository reportingRepository;

    /**
     * {@code Get  /student/daily} : generate a new report.
     *
     * @param pdfRequest the pdfRequest to generate.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new quiz, or with status {@code 400 (Bad Request)} if the quiz has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/student/daily")
    public ResponseEntity<byte[]> generatePdfReport(@Valid @RequestBody PeriodicReportPdfRequest pdfRequest)
        throws URISyntaxException, IOException, DocumentException {
        log.debug("REST request to generate pdf report : {}", pdfRequest);
        List<PeriodicReportPdfDetailInterface> pdfDetail = reportingRepository.getNativePeriodicReport();
        File pdfReportFile = pdfService.generatePdf(null, pdfRequest);
        FileInputStream fileStream = new FileInputStream(pdfReportFile);
        byte contents[] = new byte[(int) pdfReportFile.length()];
        fileStream.read(contents);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/pdf"));
        String filename = pdfRequest.getFileName();
        headers.setContentDispositionFormData(filename, filename);
        ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(contents, headers, HttpStatus.OK);
        return response;
    }
}
