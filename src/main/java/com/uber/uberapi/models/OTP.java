package com.uber.uberapi.models;

import com.uber.uberapi.exceptions.InvalidOTPException;
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

    public boolean validateEnteredOTP(OTP otp, Long expiryMinutes) {
        if(!code.equals(otp.getCode())) {
            return false;
        }
        // if the createAt + expiryMinutes > currentTime, then it is valid
        // other not
        return true;
    }
}
