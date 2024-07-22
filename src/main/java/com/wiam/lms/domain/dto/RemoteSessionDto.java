package com.wiam.lms.domain.dto;

import java.io.Serializable;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RemoteSessionDto implements Serializable {

    private static final long serialVersionUID = 1L;

    // session info
    private String title;
    private String groupName;
    private String gender;
    // session time
    private LocalDate sessionDate;
    private int day;
    private String sessionStartTime;
    private String link;
    private Boolean isActive;
    private String info;
    private Integer duration;
}
