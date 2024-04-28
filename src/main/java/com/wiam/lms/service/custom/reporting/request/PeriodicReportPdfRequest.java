package com.wiam.lms.service.custom.reporting.request;

import java.util.Date;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PeriodicReportPdfRequest {

    String fileName;
    String templateName;
    Date start;
    Date end;
    private Long sessionInstanceId;
}
