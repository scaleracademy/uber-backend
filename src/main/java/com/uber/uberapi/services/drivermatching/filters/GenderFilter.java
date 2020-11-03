package com.uber.uberapi.services.drivermatching.filters;

import com.uber.uberapi.models.Booking;
import com.uber.uberapi.models.Driver;
import com.uber.uberapi.models.Gender;
import com.uber.uberapi.services.Constants;

import java.util.List;
import java.util.stream.Collectors;

public class GenderFilter extends DriverFilter {
    public GenderFilter(Constants constants) {
        super(constants);
    }

    public List<Driver> apply(List<Driver> drivers, Booking booking) {
        // male drivers can only driver male passengers
        // for a female or non-binary passenger the driver must also be female/non-binary
        if (!getConstants().getIsETABasedFilterEnabled()) return drivers;

        Gender passengerGender = booking.getPassenger().getGender();
        return drivers.stream().filter(driver -> {
            Gender driverGender = driver.getGender();
            return !driverGender.equals(Gender.MALE) || passengerGender.equals(Gender.MALE);
        }).collect(Collectors.toList());
    }
}
