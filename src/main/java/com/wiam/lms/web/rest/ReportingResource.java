package com.wiam.lms.web.rest;

import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.PdfGraphics2D;
import com.wiam.lms.domain.Quiz;
import com.wiam.lms.repository.QuizRepository;
import com.wiam.lms.repository.search.QuizSearchRepository;
import com.wiam.lms.service.reporting.PdfService;
import com.wiam.lms.service.reporting.dto.PdfDetail;
import com.wiam.lms.service.reporting.request.PdfRequest;
import com.wiam.lms.web.rest.errors.BadRequestAlertException;
import com.wiam.lms.web.rest.errors.ElasticsearchExceptionMapper;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;
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
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

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

    /**
     * {@code Get  /student/daily} : generate a new report.
     *
     * @param pdfRequest the pdfRequest to generate.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new quiz, or with status {@code 400 (Bad Request)} if the quiz has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/student/daily")
    public ResponseEntity<byte[]> generatePdfReport(@Valid @RequestBody PdfRequest pdfRequest)
        throws URISyntaxException, DocumentException, IOException {
        log.debug("REST request to generate pdf report : {}", pdfRequest);
        PdfDetail pdfDetail = PdfDetail.builder().build();
        File pdfReportFile = pdfService.generatePdf(pdfDetail, pdfRequest);
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
