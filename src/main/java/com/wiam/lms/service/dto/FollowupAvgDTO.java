package com.wiam.lms.service.dto;

import com.wiam.lms.domain.enumeration.Tilawa;
import java.io.Serializable;

public class FollowupAvgDTO implements Serializable {

    private static final long serialVersionUID = -5781224648793484601L;

    public FollowupAvgDTO(Tilawa tilawaType, Double score) {
        this.type = tilawaType;
        this.score = score;
    }

    private Enum<Tilawa> type;
    private Double score;

    public Enum<Tilawa> getType() {
        return type;
    }

    public void setType(Enum<Tilawa> type) {
        this.type = type;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }
}
