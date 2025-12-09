package com.mryoda.diagnostics.api.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VerifyOtpRequest {
    private String mobile;
    private String country_code;
    private String otp;
}