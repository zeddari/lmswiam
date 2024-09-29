package com.wiam.lms.web.rest.vm;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * View Model object for storing a user's credentials.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VerifyToken {

    @NotNull
    private String jwtToken;
}
