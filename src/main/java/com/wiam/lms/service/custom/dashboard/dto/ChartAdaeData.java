package com.wiam.lms.service.custom.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChartAdaeData {

    private ChartAdaeSeries chartTilawaSeries;
    private ChartAdaeSeries chartMoraja3aSeries;
    private ChartAdaeSeries chartHifdSeries;
}
