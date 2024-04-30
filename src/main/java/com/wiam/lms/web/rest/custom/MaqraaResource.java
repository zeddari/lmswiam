package com.wiam.lms.web.rest.custom;

import com.lowagie.text.DocumentException;
import com.wiam.lms.domain.Session;
import com.wiam.lms.domain.enumeration.SessionMode;
import com.wiam.lms.repository.custom.DashboardRepository;
import com.wiam.lms.repository.custom.MaqraaRepository;
import com.wiam.lms.service.custom.dashboard.dto.ChartData;
import com.wiam.lms.service.custom.dashboard.dto.ChartSeries;
import com.wiam.lms.service.custom.reporting.PdfService;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing {Maqraa}.
 */
@RestController
@RequestMapping("/api/maqraa")
@Transactional
public class MaqraaResource {

    private final Logger log = LoggerFactory.getLogger(MaqraaResource.class);

    private static final String ENTITY_NAME = "NA";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    @Autowired
    private PdfService pdfService;

    @Autowired
    private MaqraaRepository maqraaRepository;

    /**
     *
     * @return
     * @throws URISyntaxException
     * @throws IOException
     * @throws DocumentException
     */
    @GetMapping("/list")
    public ResponseEntity<List<Session>> getMaqraaList() throws URISyntaxException, IOException, DocumentException {
        log.debug("REST request to getAbsenceData");
        List<Session> sessions = maqraaRepository.findBySessionMode(SessionMode.ONLINE);
        return ResponseEntity.ok(sessions);
    }
}
