package com.wiam.lms.service.custom.dashboard.dto;

import java.time.ZonedDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChartAdaeSeries {

    private String name;
    private List<Integer> data;
    private List<ZonedDateTime> date;
}
