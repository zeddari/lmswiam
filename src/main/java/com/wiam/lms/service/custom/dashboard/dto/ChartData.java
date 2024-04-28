package com.wiam.lms.service.custom.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChartData {
    private Long countItems;
    private Double deltaLastMonthPercent;
    private ChartSeries chartSeries;
}
