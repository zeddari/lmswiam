package com.wiam.lms.service.custom.dashboard.dto;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChartSeries {

    private String name;
    private List<Long> data;
}
