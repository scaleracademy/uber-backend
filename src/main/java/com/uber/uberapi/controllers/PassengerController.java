package com.uber.uberapi.controllers;

import com.uber.uberapi.exceptions.InvalidBookingException;
import com.uber.uberapi.exceptions.InvalidPassengerException;
import com.uber.uberapi.models.*;
import com.uber.uberapi.repositories.BookingRepository;
import com.uber.uberapi.repositories.PassengerRepository;
import com.uber.uberapi.repositories.ReviewRepository;
import com.uber.uberapi.services.BookingService;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/passenger")
public class PassengerController {
    // handle all operations for passenger

    final PassengerRepository passengerRepository;
    final BookingRepository bookingRepository;
    final BookingService bookingService;
    final ReviewRepository reviewRepository;

    public PassengerController(PassengerRepository passengerRepository, BookingRepository bookingRepository, BookingService bookingService, ReviewRepository reviewRepository) {
        this.passengerRepository = passengerRepository;
        this.bookingRepository = bookingRepository;
        this.bookingService = bookingService;
        this.reviewRepository = reviewRepository;
    }

    // all endpoints that the passenger can use
    public Passenger getPassengerFromId(Long passengerId) {
        Optional<Passenger> passenger = passengerRepository.findById(passengerId);
        if (passenger.isEmpty()) {
            throw new InvalidPassengerException("No passenger with id " + passengerId);
        }
        return passenger.get();
    }

    public Booking getPassengerBookingFromId(Long bookingId, Passenger passenger) {
        Optional<Booking> optionalBooking = bookingRepository.findById(bookingId);
        if (optionalBooking.isEmpty()) {
            throw new InvalidBookingException("No booking with id " + optionalBooking);
        }
        Booking booking = optionalBooking.get();
        if (!booking.getPassenger().equals(passenger)) {
            throw new InvalidBookingException("Passenger " + passenger.getBookings() + " has no such booking " + bookingId);
        }
        return booking;
    }


    @GetMapping("/{passengerId}")
    public Passenger getPassengerDetails(@PathVariable(name = "passengerId") Long passengerId) {
        // passenger 10 has authenticated
        // endpoint - /passengers/bookings
        // endpoint - /passengers/20/bookings

        // make sure that the passenger is authenticated
        // and has the same passengerId as requested
        return getPassengerFromId(passengerId);
    }

    @GetMapping("{passengerId}/bookings")
    public List<Booking> getAllBookings(@PathVariable(name = "passengerId") Long passengerId) {
        Passenger passenger = getPassengerFromId(passengerId);
        return passenger.getBookings();
    }

    @GetMapping("{passengerId}/bookings/{bookingId}")
    public Booking getBooking(@PathVariable(name = "passengerId") Long passengerId,
                              @PathVariable(name = "bookingId") Long bookingId) {
        Passenger passenger = getPassengerFromId(passengerId);
        return getPassengerBookingFromId(bookingId, passenger);
    }
    // passengers/20/bookings
    // Prime, Taj Mahal, Red Fort
    // click the schedule button
    // this controller just saves the booking details
    // it responds back - success
    // sitting on the window

    @PostMapping("{passengerId}/bookings/")
    public void requestBooking(@PathVariable(name = "passengerId") Long passengerId,
                               @RequestBody Booking data) {
        Passenger passenger = getPassengerFromId(passengerId);
        List<ExactLocation> route = new ArrayList<>();
        data.getRoute().forEach(location -> {
            route.add(ExactLocation.builder()
                    .latitude(location.getLatitude())
                    .longitude(location.getLongitude())
                    .build());
        });

        Booking booking = Booking.builder()
                .rideStartOTP(OTP.make(passenger.getPhoneNumber()))
                .route(route)
                .passenger(passenger)
                .bookingType(data.getBookingType())
                .scheduledTime(data.getScheduledTime())
                .build();
        bookingService.createBooking(booking);
    }

    @PatchMapping("{passengerId}/bookings/{bookingId}")
    public void updateRoute(@PathVariable(name = "passengerId") Long passengerId,
                            @PathVariable(name = "bookingId") Long bookingId,
                            @RequestBody Booking data) {
        Passenger passenger = getPassengerFromId(passengerId);
        Booking booking = getPassengerBookingFromId(bookingId, passenger);
        List<ExactLocation> route = new ArrayList<>(booking.getCompletedRoute());
        data.getRoute().forEach(location -> {
            route.add(ExactLocation.builder()
                    .latitude(location.getLatitude())
                    .longitude(location.getLongitude())
                    .build());
        });
        bookingService.updateRoute(booking, route);
    }

    // passenger requests booking
    // saved to db
    // message sent to drivermatching service
    // consume the message
    // find the drivers
    // if none are available
    // passenger will be notified
    // passenger might retry to find drivers

    // restaurant - waiter analogy
    // Controller = waiters

    @PostMapping("{passengerId}/bookings/{bookingId}")
    public void retryBooking(@PathVariable(name = "passengerId") Long passengerId,
                             @PathVariable(name = "bookingId") Long bookingId) {
        Passenger passenger = getPassengerFromId(passengerId);
        Booking booking = getPassengerBookingFromId(bookingId, passenger);
        bookingService.retryBooking(booking);
    }

    @DeleteMapping("{passengerId}/bookings/{bookingId}")
    public void cancelBooking(@PathVariable(name = "passengerId") Long passengerId,
                              @PathVariable(name = "bookingId") Long bookingId) {
        Passenger passenger = getPassengerFromId(passengerId);
        Booking booking = getPassengerBookingFromId(bookingId, passenger);
        bookingService.cancelByPassenger(passenger, booking);
    }

    @PatchMapping("{passengerId}/bookings/{bookingId}/rate")
    public void rateRide(@PathVariable(name = "passengerId") Long passengerId,
                         @PathVariable(name = "bookingId") Long bookingId,
                         @RequestBody Review data) {
        // gets json data in the body
        Passenger passenger = getPassengerFromId(passengerId);
        Booking booking = getPassengerBookingFromId(bookingId, passenger);
        Review review = Review.builder()
                .note(data.getNote())
                .ratingOutOfFive(data.getRatingOutOfFive())
                .build();
        booking.setReviewByPassenger(review);
        reviewRepository.save(review);
        bookingRepository.save(booking);
    }


}
