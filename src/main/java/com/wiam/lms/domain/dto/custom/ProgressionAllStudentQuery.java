package com.wiam.lms.domain.dto.custom;

import java.time.LocalDate;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProgressionAllStudentQuery {

    LocalDate startDate;
    LocalDate endDate;
    Long sessionInstanceId;
}
