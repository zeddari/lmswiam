package com.wiam.lms.domain.dto.custom;

import com.wiam.lms.domain.Progression;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ProgressionRepresentation {

    private String studentProgressionLabel;
    private List<Progression> progression;
}
