package com.uber.uberapi.exceptions;

public class InvalidOTPException extends UberException {
    /**
     * Constructs a new runtime exception with the specified detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     */
    public InvalidOTPException() {
        super("Invalid OTP");
    }
}
