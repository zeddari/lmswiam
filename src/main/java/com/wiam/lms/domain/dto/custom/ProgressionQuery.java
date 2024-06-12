package com.wiam.lms.domain.dto.custom;

import java.util.Date;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProgressionQuery {

    Date startDate;
    Date endDate;
    Long studentId;
    Long siteId;
}
