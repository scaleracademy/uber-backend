package com.uber.uberapi.services;

import com.uber.uberapi.models.Booking;
import com.uber.uberapi.models.Driver;
import com.uber.uberapi.models.Passenger;
import org.springframework.stereotype.Service;

@Service
public class SimpleDriverMatchingService implements DriverMatchingService {
    @Override
    public void acceptBooking(Driver driver, Booking booking) {

    }

    @Override
    public void cancelByDriver(Driver driver, Booking booking) {

    }

    @Override
    public void cancelByPassenger(Passenger passenger, Booking booking) {

    }

    @Override
    public void assignDriver(Booking booking) {

    }

    public static void main(String[] args) {
        // consumer
        // for each request
        // call the appropriate method
    }
}
