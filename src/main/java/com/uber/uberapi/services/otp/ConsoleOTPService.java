package com.uber.uberapi.services.otp;

import com.uber.uberapi.models.OTP;
import org.springframework.stereotype.Service;

@Service
public class ConsoleOTPService implements OTPService {
    @Override
    public void sendPhoneNumberConfirmationOTP(OTP otp) {
        System.out.printf("Confirm phone number %s: OTP - %s", otp.getSentToNumber(), otp.getCode());
    }

    @Override
    public void sendRideStartOTP(OTP otp) {
        System.out.printf("%s - Share this OTP with your driver to start the ride %s", otp.getSentToNumber(), otp.getCode());
    }
}
