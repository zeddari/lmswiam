package com.wiam.lms.service.custom.dashboard.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ChartSeries {
    private String name;
    private List<Long> data;
}
