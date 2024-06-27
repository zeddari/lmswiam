package com.wiam.lms.domain.dto.custom;

import java.util.Date;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SessionInstanceQuery {

    private List<Long> sessionIds;
    private Date startDate;
    private Date endDate;
}
