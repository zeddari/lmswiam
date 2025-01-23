package com.wiam.lms.domain.statistics;

import com.wiam.lms.domain.enumeration.SessionType;

public class SessionTypeCountDTO {

    private SessionType sessionType;
    private Long count;

    public SessionTypeCountDTO(SessionType sessionType, Long count) {
        this.sessionType = sessionType;
        this.count = count;
    }

    // Getters and setters
    public SessionType getSessionType() {
        return sessionType;
    }

    public void setSessionType(SessionType sessionType) {
        this.sessionType = sessionType;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}
