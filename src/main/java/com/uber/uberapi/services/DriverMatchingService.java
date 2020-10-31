package com.uber.uberapi.services;

import com.uber.uberapi.models.Booking;
import com.uber.uberapi.models.Driver;
import com.uber.uberapi.models.Passenger;

public interface DriverMatchingService {
    void acceptBooking(Driver driver, Booking booking);

    void cancelByDriver(Driver driver, Booking booking);

    void cancelByPassenger(Passenger passenger, Booking booking);

    void assignDriver(Booking booking);
    // figure out what drivers are nearby
    // send notifications to them
}
