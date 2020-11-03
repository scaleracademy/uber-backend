package com.uber.uberapi.services;

import com.uber.uberapi.models.ExactLocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ETAService {
    @Autowired
    private Constants constants;

    public int getETAMinutes(ExactLocation lastKnownLocation, ExactLocation pickup) {
        return (int) (60.0 * lastKnownLocation.distanceKm(pickup) / constants.getDefaultETASpeedKmph());
    }
}
