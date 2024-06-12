package com.wiam.lms.domain.dto.custom;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserCustomDto {

    private String jwt;
    private List<String> roles;
    private Long studentId;
    private Long siteId;
}
