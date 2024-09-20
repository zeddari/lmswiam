package com.wiam.lms.service.custom.dashboard.dto;

import java.time.ZonedDateTime;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class LastAbsencesData {

    private Long id;
    private String name;
    private String title;
    private ZonedDateTime date;
}
