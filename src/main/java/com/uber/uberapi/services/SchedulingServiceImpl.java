package com.uber.uberapi.services;

import com.uber.uberapi.models.Booking;
import org.springframework.stereotype.Service;

@Service
public class SchedulingServiceImpl implements SchedulingService {
    BookingService bookingService;

    public static void main(String[] args) {
        // kafka consumer
    }

    @Override
    public void schedule(Booking booking) {
        // if it is time to activate this booking
        bookingService.acceptBooking(booking.getDriver(), booking);
    }
}

// PubSub design pattern

// Observer Design Pattern -> events
