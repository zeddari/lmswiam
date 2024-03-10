package com.wiam.lms.service.reporting.request;

import java.util.Date;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PdfRequest {

    String fileName;
    String templateName;
    Date start;
    Date end;
}
