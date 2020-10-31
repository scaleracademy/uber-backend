package com.uber.uberapi.services;

import com.uber.uberapi.models.Booking;

public interface SchedulingService {
    void schedule(Booking booking);
}
