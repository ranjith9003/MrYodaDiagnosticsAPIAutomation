package com.mryoda.diagnostics.api.payloads;

import org.json.JSONObject;
import com.mryoda.diagnostics.api.utils.RequestContext;
import com.mryoda.diagnostics.api.config.ConfigLoader;

public class OTPPayloadBuilder {

    public static JSONObject buildSendOTP() {
        JSONObject body = new JSONObject();
        body.put("country_code", ConfigLoader.getConfig().countryCode());
        body.put("mobile", RequestContext.getMobile());
        return body;
    }

    public static JSONObject buildVerifyOTP(String otp) {
        JSONObject body = buildSendOTP();
        body.put("otp", otp);
        return body;
    }
}
