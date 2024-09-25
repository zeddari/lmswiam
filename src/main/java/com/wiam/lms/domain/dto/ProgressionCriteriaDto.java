package com.wiam.lms.domain.dto;

import com.wiam.lms.domain.enumeration.Attendance;
import com.wiam.lms.domain.enumeration.ExamType;
import com.wiam.lms.domain.enumeration.Riwayats;
import com.wiam.lms.domain.enumeration.Tilawa;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProgressionCriteriaDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private LocalDate sessionDate;
    private Integer id;
    private String examType;
}
