package com.wiam.lms.service.dto;

import com.wiam.lms.domain.enumeration.Tilawa;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FollowupListDTO implements Serializable {

    private static final long serialVersionUID = 8528209882179547726L;

    public FollowupListDTO(Enum<Tilawa> type, Object followupsObject) {
        String follows = followupsObject.toString();
        this.type = type;
        Arrays
            .stream(follows.split(","))
            .forEach(s -> this.followups.add(new FollowupByDateDTO(s.split("#")[0], Double.parseDouble(s.split("#")[1]))));
    }

    private Enum<Tilawa> type;
    private List<FollowupByDateDTO> followups = new ArrayList<FollowupByDateDTO>();

    public Enum<Tilawa> getType() {
        return type;
    }

    public void setType(Enum<Tilawa> type) {
        this.type = type;
    }

    public List<FollowupByDateDTO> getFollowups() {
        return followups;
    }

    public void setFollowups(List<FollowupByDateDTO> followups) {
        this.followups = followups;
    }
}
