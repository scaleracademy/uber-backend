package com.uber.uberapi.services;

import com.uber.uberapi.models.Booking;
import com.uber.uberapi.models.Driver;
import com.uber.uberapi.models.OTP;

public interface OTPService {
    void sendPhoneNumberConfirmationOTP(OTP otp);
    void sendRideStartOTP(OTP otp);
}
