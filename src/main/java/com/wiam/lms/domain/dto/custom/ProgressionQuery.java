package com.wiam.lms.domain.dto.custom;

import java.time.LocalDate;
import java.util.Date;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProgressionQuery {

    LocalDate startDate;
    LocalDate endDate;
    Long studentId;
    Long siteId;
}
