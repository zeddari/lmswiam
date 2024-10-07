package com.wiam.lms.service.custom.reporting.request;

import java.util.Date;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PeriodicReportRequest {

    private Date start;
    private Date end;
    private Long sessionId;
}
