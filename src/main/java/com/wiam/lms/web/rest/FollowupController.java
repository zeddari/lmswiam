package com.wiam.lms.web.rest;

import com.wiam.lms.repository.ProgressionRepository;
import com.wiam.lms.repository.custom.ProgressionCustomRepository;
import com.wiam.lms.service.dto.FollowupAvgDTO;
import com.wiam.lms.service.dto.FollowupListDTO;
import java.time.LocalDate;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/followup")
public class FollowupController {

    private static final String DATE_FORMAT_PATTERN = "yyyyMMdd";

    private static final Logger LOG = LoggerFactory.getLogger(ProgressionResource.class);

    private final ProgressionCustomRepository progressionRepository;

    public FollowupController(ProgressionCustomRepository progressionRepository) {
        this.progressionRepository = progressionRepository;
    }

    @GetMapping("/averages/{studentId}")
    public List<FollowupAvgDTO> getStudentFollowupAvgByDateRange(
        @PathVariable Long studentId,
        @RequestParam("startDate") @DateTimeFormat(pattern = DATE_FORMAT_PATTERN) LocalDate startDate,
        @RequestParam("endDate") @DateTimeFormat(pattern = DATE_FORMAT_PATTERN) LocalDate endDate
    ) {
        LOG.debug("REST request to search getStudentFollowupScoresAvgByDateRange for params {},{},{}", studentId, startDate, endDate);
        return progressionRepository.followupStudentScores(studentId, startDate, endDate);
    }

    @GetMapping("/list/{studentId}")
    public List<FollowupListDTO> getStudentFollowupListByDateRange(
        @PathVariable Long studentId,
        @RequestParam("startDate") @DateTimeFormat(pattern = DATE_FORMAT_PATTERN) LocalDate startDate,
        @RequestParam("endDate") @DateTimeFormat(pattern = DATE_FORMAT_PATTERN) LocalDate endDate
    ) {
        LOG.debug("REST request to search getStudentFollowupScoresAvgByDateRange for params {},{},{}", studentId, startDate, endDate);
        return progressionRepository.followupStudentList(studentId, startDate, endDate);
    }
}
