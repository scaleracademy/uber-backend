package com.uber.uberapi.models;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "otp")
public class OTP extends Auditable {
    private String code;
    private String sentToNumber;

    public static OTP make(String phoneNumber) {
        return OTP.builder()
                .code("0000") // random number generator
                .sentToNumber(phoneNumber)
                .build();
    }

    public boolean validateEnteredOTP(OTP otp, Integer expiryMinutes) {
        if (!code.equals(otp.getCode())) {
            return false;
        }
        // if the createAt + expiryMinutes > currentTime, then it is valid
        // other not
        return true;
    }
}
