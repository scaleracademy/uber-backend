package com.uber.uberapi.services.drivermatching.filters;

import com.uber.uberapi.models.Booking;
import com.uber.uberapi.models.Driver;
import com.uber.uberapi.models.ExactLocation;
import com.uber.uberapi.services.Constants;
import com.uber.uberapi.services.ETAService;

import java.util.List;
import java.util.stream.Collectors;

public class VIPFilter extends DriverFilter {
    private final ETAService etaService;

    public VIPFilter(ETAService etaService, Constants constants) {
        super(constants);
        this.etaService = etaService;
    }

    public List<Driver> apply(List<Driver> drivers, Booking booking) {
        if (!getConstants().getIsETABasedFilterEnabled()) return drivers;
        // if the booking is for a prime or Sedan, then only match drivers > 4 rating
        // todo
        // for each driver, find the avg rating
        return drivers;
    }
}
