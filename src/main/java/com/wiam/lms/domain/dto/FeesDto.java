package com.wiam.lms.domain.dto;

import com.wiam.lms.domain.enumeration.PaymentSide;
import com.wiam.lms.domain.enumeration.PaymentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.ZonedDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FeesDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private ZonedDateTime StartDate;
    private ZonedDateTime endDate;


}
