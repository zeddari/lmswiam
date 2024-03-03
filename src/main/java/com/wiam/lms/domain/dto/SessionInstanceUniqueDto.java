package com.wiam.lms.domain.dto;

import java.io.Serializable;
import java.time.LocalDate;

public class SessionInstanceUniqueDto implements Serializable {

    private static final long serialVersionUID = 1L;

    public SessionInstanceUniqueDto() {}

    private Long sessionId;

    private Long groupId;

    public Long getGroupId() {
        return this.groupId;
    }

    public void setGroupId(long ID) {
        this.groupId = ID;
    }

    private LocalDate sessionDate;

    public Long getSessionId() {
        return sessionId;
    }

    public LocalDate getSessionDate() {
        return sessionDate;
    }

    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }

    public void setSessionDate(LocalDate sessionDate) {
        this.sessionDate = sessionDate;
    }
}
