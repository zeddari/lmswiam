package com.wiam.lms.domain.dto;

import java.time.LocalDate;
import java.util.List;

public class InstanceProgressionDTO {

    private Long id;
    private LocalDate sessionDate;
    private String title;
    private List<ProgressionDetailsDTO> progressions;
    private Long sessionId;

    // Default constructor
    public InstanceProgressionDTO() {}

    // Constructor with all fields

    // Constructor for JPA query
    public InstanceProgressionDTO(String title) {
        this.title = title;
    }

    // Constructor with all fields
    public InstanceProgressionDTO(Long id, String title, LocalDate sessionDate, List<ProgressionDetailsDTO> progressions, Long sessionId) {
        this.id = id;
        this.title = title;
        this.sessionDate = sessionDate;
        this.progressions = progressions;
        this.sessionId = sessionId;
    }

    // Existing getters and setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<ProgressionDetailsDTO> getProgressions() {
        return progressions;
    }

    public void setProgressions(List<ProgressionDetailsDTO> progressions) {
        this.progressions = progressions;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getSessionDate() {
        return sessionDate;
    }

    public void setSessionDate(LocalDate sessionDate) {
        this.sessionDate = sessionDate;
    }

    public Long getSessionId() {
        return sessionId;
    }

    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }
}
