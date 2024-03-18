package com.wiam.lms.service.dto;

import java.io.Serializable;

public class FollowupByDateDTO implements Serializable {

    private static final long serialVersionUID = -205785288528660071L;

    public FollowupByDateDTO(String date, Double score) {
        this.date = date;
        this.score = score;
    }

    private String date;
    private Double score;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }
}
