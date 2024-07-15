package com.wiam.lms.web.rest.custom;

import com.lowagie.text.DocumentException;
import com.wiam.lms.domain.UserCustom;
import com.wiam.lms.repository.custom.ReportingRepository;
import com.wiam.lms.service.custom.reporting.PdfService;
import com.wiam.lms.service.custom.reporting.request.CertificatePdfRequest;
import jakarta.validation.Valid;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
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
 * REST controller for managing {@link UserCustom}.
 */
@RestController
@RequestMapping("/api/certificates/custom")
@Transactional
public class CertificateCustomResource {

    private final Logger log = LoggerFactory.getLogger(CertificateCustomResource.class);

    private static final String ENTITY_NAME = "userCustom";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    @Autowired
    private ReportingRepository reportingRepository;

    @Autowired
    private PdfService pdfService;

    @PostMapping("/generate")
    public ResponseEntity<byte[]> generatePdfReport(@Valid @RequestBody CertificatePdfRequest pdfRequest)
        throws URISyntaxException, IOException, DocumentException {
        log.debug("REST request to generate certificate pdf report : {}", pdfRequest);
        File pdfReportFile = pdfService.generateCertificatePdf(pdfRequest.getStudentName(), pdfRequest);
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
