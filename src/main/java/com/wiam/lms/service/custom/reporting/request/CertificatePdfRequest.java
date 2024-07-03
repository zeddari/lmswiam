package com.wiam.lms.service.custom.reporting.request;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class CertificatePdfRequest {

    String fileName;
    String templateName;
    String studentName;
}
