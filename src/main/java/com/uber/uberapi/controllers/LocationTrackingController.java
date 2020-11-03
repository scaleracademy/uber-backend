package com.uber.uberapi.controllers;

import com.uber.uberapi.exceptions.InvalidDriverException;
import com.uber.uberapi.exceptions.InvalidPassengerException;
import com.uber.uberapi.models.Driver;
import com.uber.uberapi.models.ExactLocation;
import com.uber.uberapi.models.Passenger;
import com.uber.uberapi.repositories.DriverRepository;
import com.uber.uberapi.repositories.PassengerRepository;
import com.uber.uberapi.services.Constants;
import com.uber.uberapi.services.locationtracking.LocationTrackingService;
import com.uber.uberapi.services.messagequeue.MessageQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/location")
public class LocationTrackingController {
    @Autowired
    DriverRepository driverRepository;
    @Autowired
    PassengerRepository passengerRepository;
    @Autowired
    LocationTrackingService locationTrackingService;
    @Autowired
    MessageQueue messageQueue;
    @Autowired
    Constants constants;

    public Driver getDriverFromId(Long driverId) {
        Optional<Driver> driver = driverRepository.findById(driverId);
        if (driver.isEmpty()) {
            throw new InvalidDriverException("No driver with id " + driverId);
        }
        return driver.get();
    }

    @PutMapping("/driver/{driverId}")
    public void updateDriverLocation(@PathVariable Long driverId,
                                     @RequestBody ExactLocation data) {
        // once every 3 seconds for each active driver
        Driver driver = getDriverFromId(driverId);
        // todo: check if the driver has an active booking
        //       update the bookings completedRoute based on the driver's location
        //       update the expected completion time

        ExactLocation location = ExactLocation.builder()
                .longitude(data.getLongitude())
                .latitude(data.getLatitude())
                .build();
        messageQueue.sendMessage(
                constants.getLocationTrackingTopicName(),
                new LocationTrackingService.Message(driver, location)
        );
    }

    public Passenger getPassengerFromId(Long passengerId) {
        Optional<Passenger> passenger = passengerRepository.findById(passengerId);
        if (passenger.isEmpty()) {
            throw new InvalidPassengerException("No passenger with id " + passengerId);
        }
        return passenger.get();
    }

    @PutMapping("/passenger/{passengerId}")
    public void updatePassengerLocation(@PathVariable Long passengerId,
                                        @RequestBody ExactLocation location) {
        // only triggers every 30 secs if the passenger is active
        Passenger passenger = getPassengerFromId(passengerId);
        passenger.setLastKnownLocation(ExactLocation.builder()
                .longitude(location.getLongitude())
                .latitude(location.getLatitude())
                .build());
        passengerRepository.save(passenger);
    }
}
