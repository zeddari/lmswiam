package com.wiam.lms.service.custom.reporting.request;

import java.util.Date;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CertificatePdfRequest {

    String fileName;
    String templateName;
    String studentName;
    String type;
}
