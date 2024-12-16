package com.wiam.lms.domain.dto.custom;

import java.io.Serializable;

public class SessionDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String SessionName;

    public void setId(Long id) {
        this.id = id;
    }

    public void setSessionName(String sessionName) {
        SessionName = sessionName;
    }

    public Long getId() {
        return id;
    }

    public String getSessionName() {
        return SessionName;
    }
}
