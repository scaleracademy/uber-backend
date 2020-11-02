package com.uber.uberapi.services.otp;

import com.uber.uberapi.models.OTP;

public interface OTPService {
    void sendPhoneNumberConfirmationOTP(OTP otp);

    void sendRideStartOTP(OTP otp);
}
