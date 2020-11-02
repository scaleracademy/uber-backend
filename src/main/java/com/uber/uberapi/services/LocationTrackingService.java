package com.uber.uberapi.services;

import com.uber.uberapi.models.Driver;
import com.uber.uberapi.models.ExactLocation;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationTrackingService {
    public List<Driver> getDriversNearLocation(ExactLocation pickup) {
        return null; // todo
    }
}
