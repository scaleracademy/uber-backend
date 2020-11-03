package com.uber.uberapi.controllers;

import com.uber.uberapi.exceptions.InvalidBookingException;
import com.uber.uberapi.exceptions.InvalidDriverException;
import com.uber.uberapi.models.Booking;
import com.uber.uberapi.models.Driver;
import com.uber.uberapi.models.OTP;
import com.uber.uberapi.models.Review;
import com.uber.uberapi.repositories.BookingRepository;
import com.uber.uberapi.repositories.DriverRepository;
import com.uber.uberapi.repositories.ReviewRepository;
import com.uber.uberapi.services.BookingService;
import com.uber.uberapi.services.Constants;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequestMapping("/driver")
@RestController
public class DriverController {
    // all endpoints that the driver can use

    final DriverRepository driverRepository;
    final BookingRepository bookingRepository;
    final BookingService bookingService;
    final ReviewRepository reviewRepository;
    final Constants constants;

    public DriverController(DriverRepository driverRepository, BookingRepository bookingRepository, BookingService bookingService, ReviewRepository reviewRepository, Constants constants) {
        this.driverRepository = driverRepository;
        this.bookingRepository = bookingRepository;
        this.bookingService = bookingService;
        this.reviewRepository = reviewRepository;
        this.constants = constants;
    }

    public Driver getDriverFromId(Long driverId) {
        Optional<Driver> driver = driverRepository.findById(driverId);
        if (driver.isEmpty()) {
            throw new InvalidDriverException("No driver with id " + driverId);
        }
        return driver.get();
    }

    private Booking getBookingFromId(Long bookingId) {
        Optional<Booking> booking = bookingRepository.findById(bookingId);
        if (booking.isEmpty()) {
            throw new InvalidBookingException("No booking with id " + booking);
        }
        return booking.get();
    }

    public Booking getDriverBookingFromId(Long bookingId, Driver driver) {
        Booking booking = getBookingFromId(bookingId);
        if (!booking.getDriver().equals(driver)) {
            throw new InvalidBookingException("Driver " + driver.getBookings() + " has no such booking " + bookingId);
        }
        return booking;
    }


    @GetMapping("/{driverId}")
    public Driver getDriverDetails(@PathVariable(name = "driverId") Long driverId) {
        return getDriverFromId(driverId);
    }

    @PatchMapping("/{driverId}")
    public void changeAvailability(@PathVariable(name = "driverId") Long driverId,
                                   @RequestBody Boolean available) {
        Driver driver = getDriverFromId(driverId);
        driver.setIsAvailable(available);
        driverRepository.save(driver);
    }

    @GetMapping("{driverId}/bookings")
    public List<Booking> getAllBookings(@PathVariable(name = "driverId") Long driverId) {
        Driver driver = getDriverFromId(driverId);
        return driver.getBookings();
    }

    @GetMapping("{driverId}/bookings/{bookingId}")
    public Booking getBooking(@PathVariable(name = "driverId") Long driverId,
                              @PathVariable(name = "bookingId") Long bookingId) {
        Driver driver = getDriverFromId(driverId);
        // driver can only see bookings that they're the driver for
        return getDriverBookingFromId(bookingId, driver);
    }

    // 500: Internal Server Error
    // Driver is forbidden to see that booking
    // Driver is trying to perform an incorrect action

    @PostMapping("{driverId}/bookings/{bookingId}")
    public void acceptBooking(@PathVariable(name = "driverId") Long driverId,
                              @PathVariable(name = "bookingId") Long bookingId) {
        Driver driver = getDriverFromId(driverId);
        // driver can only see bookings that they're the driver for
        Booking booking = getBookingFromId(bookingId);
        bookingService.acceptBooking(driver, booking);
    }

    @DeleteMapping("{driverId}/bookings/{bookingId}")
    public void cancelBooking(@PathVariable(name = "driverId") Long driverId,
                              @PathVariable(name = "bookingId") Long bookingId) {
        Driver driver = getDriverFromId(driverId);
        Booking booking = getDriverBookingFromId(bookingId, driver);
        bookingService.cancelByDriver(driver, booking);
    }

    // rate the booking
    // start the ride
    // end the ride

    @PatchMapping("{driverId}/bookings/{bookingId}/start")
    public void startRide(@PathVariable(name = "driverId") Long driverId,
                          @PathVariable(name = "bookingId") Long bookingId,
                          @RequestBody OTP otp) {
        Driver driver = getDriverFromId(driverId);
        Booking booking = getDriverBookingFromId(bookingId, driver);
        booking.startRide(otp, constants.getRideStartOTPExpiryMinutes());
        bookingRepository.save(booking);
    }

    @PatchMapping("{driverId}/bookings/{bookingId}/end")
    public void endRide(@PathVariable(name = "driverId") Long driverId,
                        @PathVariable(name = "bookingId") Long bookingId) {
        Driver driver = getDriverFromId(driverId);
        Booking booking = getDriverBookingFromId(bookingId, driver);
        booking.endRide();
        driverRepository.save(driver);
        bookingRepository.save(booking);
    }

    @PatchMapping("{driverId}/bookings/{bookingId}/rate")
    public void rateRide(@PathVariable(name = "driverId") Long driverId,
                         @PathVariable(name = "bookingId") Long bookingId,
                         @RequestBody Review data) {
        // gets json data in the body
        Driver driver = getDriverFromId(driverId);
        Booking booking = getDriverBookingFromId(bookingId, driver);
        Review review = Review.builder()
                .note(data.getNote())
                .ratingOutOfFive(data.getRatingOutOfFive())
                .build();
        booking.setReviewByDriver(review);
        reviewRepository.save(review);
        bookingRepository.save(booking);
    }
}


// Controllers -> models / services
// Services -> other services / other controllers / models
// models(DAO) -> DB
// repositories(DAL) -> manage the models