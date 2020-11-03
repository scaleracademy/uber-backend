package com.uber.uberapi.services.drivermatching.filters;

import com.uber.uberapi.models.Booking;
import com.uber.uberapi.models.Driver;
import com.uber.uberapi.services.Constants;
import lombok.Getter;

import java.util.List;

public abstract class DriverFilter {
    @Getter
    private final Constants constants;

    public DriverFilter(Constants constants) {
        this.constants = constants;
    }

    public abstract List<Driver> apply(List<Driver> drivers, Booking booking);
}
