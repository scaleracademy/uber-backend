package com.uber.uberapi.services;

import com.uber.uberapi.models.Booking;
import com.uber.uberapi.models.BookingStatus;
import com.uber.uberapi.models.Driver;
import com.uber.uberapi.models.Passenger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class DefaultBookingService implements BookingService {
    @Autowired
    DriverMatchingService driverMatchingService;
    @Autowired
    SchedulingService schedulingService;
    @Autowired
    OTPService otpService;

    @Override
    public void createBooking(Booking booking) {
        if (booking.getStartTime().after(new Date())) {
            booking.setBookingStatus(BookingStatus.SCHEDULED);
            {
                // office-boy
                // start an async process that will process the booking
                // producer
                // use a task queue to push this task
                schedulingService.schedule(booking);
            }
        } else {
            booking.setBookingStatus(BookingStatus.ASSIGNING_DRIVER);
            otpService.sendRideStartOTP(booking.getRideStartOTP());
            {
                // office-boy
                // start an async process that will process the booking
                // producer
                // use a task queue to push this task
                driverMatchingService.assignDriver(booking);
            }
        }
    }

    @Override
    public void acceptBooking(Driver driver, Booking booking) {
        // notify the passenger
    }

    @Override
    public void cancelByDriver(Driver driver, Booking booking) {

    }

    @Override
    public void cancelByPassenger(Passenger passenger, Booking booking) {

    }
}
