package com.wiam.lms.web.rest.custom;

import com.wiam.lms.domain.Group;
import com.wiam.lms.domain.Progression;
import com.wiam.lms.domain.SessionInstance;
import com.wiam.lms.domain.UserCustom;
import com.wiam.lms.domain.dto.ProgressionCriteriaDto;
import com.wiam.lms.domain.dto.custom.ProgressionAdminRepresentation;
import com.wiam.lms.domain.dto.custom.ProgressionQuery;
import com.wiam.lms.domain.dto.custom.ProgressionRepresentation;
import com.wiam.lms.domain.enumeration.Attendance;
import com.wiam.lms.domain.enumeration.ExamType;
import com.wiam.lms.domain.enumeration.Riwayats;
import com.wiam.lms.domain.enumeration.Tilawa;
import com.wiam.lms.repository.GroupRepository;
import com.wiam.lms.repository.ProgressionRepository;
import com.wiam.lms.repository.SessionInstanceRepository;
import com.wiam.lms.repository.search.ProgressionSearchRepository;
import com.wiam.lms.service.custom.progression.ProgressionCustomService;
import com.wiam.lms.web.rest.errors.BadRequestAlertException;
import com.wiam.lms.web.rest.errors.ElasticsearchExceptionMapper;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link Progression}.
 */
@RestController
@RequestMapping("/api/progressions/custom")
@Transactional
public class ProgressionCustomResource {

    private final Logger log = LoggerFactory.getLogger(ProgressionCustomResource.class);

    private static final String ENTITY_NAME = "progression";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProgressionRepository progressionRepository;

    private final SessionInstanceRepository sessionInstanceRepository;

    private final ProgressionSearchRepository progressionSearchRepository;

    private final GroupRepository groupRepository;

    @Autowired
    private ProgressionCustomService progressionCustomService;

    public ProgressionCustomResource(
        GroupRepository groupRepository,
        SessionInstanceRepository sessionInstanceRepository,
        ProgressionRepository progressionRepository,
        ProgressionSearchRepository progressionSearchRepository
    ) {
        this.progressionRepository = progressionRepository;
        this.progressionSearchRepository = progressionSearchRepository;
        this.sessionInstanceRepository = sessionInstanceRepository;
        this.groupRepository = groupRepository;
    }

    @GetMapping("/{id}/byStudent")
    public List<Progression> getProgressionsByStudent(@PathVariable Long id) {
        log.debug("REST request to get the Progressions by student : {}", id);
        List<Progression> progressions = progressionRepository.findAllByStudent(id);
        return progressions;
    }

    @PostMapping("/criteria/search")
    public List<ProgressionAdminRepresentation> getProgressionsByCriterias(@RequestBody ProgressionCriteriaDto progressionCriteriaDto) {
        log.debug("REST request to get the Progressions by criterias : {}", progressionCriteriaDto.getId());
        List<Progression> progressions = progressionRepository.findAllBySessionIdAndDate(
            progressionCriteriaDto.getSessionDate(),
            progressionCriteriaDto.getId()
        );
        Set<String> studentsName = new HashSet<>();
        List<Progression> myProgressions = new ArrayList<>();
        List<ProgressionAdminRepresentation> progressionAdminRepresentations = new ArrayList<>();
        if (progressions != null) {
            ProgressionAdminRepresentation progressionAdminRepresentation = ProgressionAdminRepresentation.builder().build();
            for (Progression p : progressions) {
                if (studentsName.add(p.getStudent().getLogin())) {
                    if (
                        progressionAdminRepresentation.getStudentName() != null &&
                        !progressionAdminRepresentation.getStudentName().isBlank()
                    ) {
                        progressionAdminRepresentation.setProgressions(myProgressions);
                        progressionAdminRepresentations.add(progressionAdminRepresentation);
                        myProgressions = new ArrayList<>();
                    }
                    progressionAdminRepresentation = ProgressionAdminRepresentation.builder().build();
                    progressionAdminRepresentation.setStudentName(p.getStudent().getLogin());
                    myProgressions.add(p);
                } else {
                    myProgressions.add(p);
                }
            }
        }
        return progressionAdminRepresentations;
    }

    @GetMapping("/{id}/byStudent/last")
    public ProgressionRepresentation getLastProgressionsByStudent(@PathVariable Long id) {
        log.debug("REST request to get the Progressions by student : {}", id);
        List<Progression> progressions = progressionRepository.findAllLastByStudent(id);
        ProgressionRepresentation progressionRepresentation = ProgressionRepresentation
            .builder()
            .studentProgressionLabel(progressionCustomService.calculateProgressLabel(progressions))
            .progression(progressions)
            .build();
        return progressionRepresentation;
    }

    @PostMapping("/byStudent/byDateRange")
    public List<Progression> getProgressionsByStudent(@RequestBody ProgressionQuery progressionQuery) {
        log.debug("REST request to get the Progressions by student by date range : {}", progressionQuery.getStartDate());
        List<Progression> progressions = progressionRepository.findAllByStudentIdAndSessionInstanceBetween(
            progressionQuery.getStudentId(),
            progressionQuery.getStartDate(),
            progressionQuery.getEndDate()
        );
        return progressions;
    }
}
