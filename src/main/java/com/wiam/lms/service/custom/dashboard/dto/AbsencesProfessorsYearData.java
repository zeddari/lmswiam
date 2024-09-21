package com.wiam.lms.service.custom.dashboard.dto;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AbsencesProfessorsYearData {

    private List<String> months;
    private ChartSeries chartSeries;
}
