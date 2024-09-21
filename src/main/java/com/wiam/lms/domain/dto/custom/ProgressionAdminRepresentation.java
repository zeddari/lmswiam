package com.wiam.lms.domain.dto.custom;

import com.wiam.lms.domain.Progression;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ProgressionAdminRepresentation {

    private String studentName;
    private List<Progression> progressions;
}
