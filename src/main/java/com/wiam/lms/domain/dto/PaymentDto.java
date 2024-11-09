package com.wiam.lms.domain.dto;

import com.wiam.lms.domain.enumeration.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private PaymentSide paymentSide;
    private PaymentType paymentType;
    private Long siteId;
    private ZonedDateTime StartDate;
    private ZonedDateTime endDate;


}
