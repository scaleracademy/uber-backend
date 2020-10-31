package com.uber.uberapi.controllers;

import com.uber.uberapi.exceptions.InvalidBookingException;
import com.uber.uberapi.models.Booking;
import com.uber.uberapi.models.Passenger;
import com.uber.uberapi.models.Review;
import com.uber.uberapi.repositories.BookingRepository;
import com.uber.uberapi.repositories.PassengerRepository;
import com.uber.uberapi.repositories.ReviewRepository;
import com.uber.uberapi.services.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/passenger")
public class PassengerController {
    // handle all operations for passenger

    @Autowired
    PassengerRepository passengerRepository;
    @Autowired
    BookingRepository bookingRepository;
    @Autowired
    BookingService bookingService;
    @Autowired
    ReviewRepository reviewRepository;

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
    public Passenger getPassengerDetails(@RequestParam(name = "passengerId") Long passengerId) {
        // passenger 10 has authenticated
        // endpoint - /passengers/bookings
        // endpoint - /passengers/20/bookings

        // make sure that the passenger is authenticated
        // and has the same passengerId as requested
        return getPassengerFromId(passengerId);
    }

    @GetMapping("{passengerId}/bookings")
    public List<Booking> getAllBookings(@RequestParam(name = "passengerId") Long passengerId) {
        Passenger passenger = getPassengerFromId(passengerId);
        return passenger.getBookings();
    }

    @GetMapping("{passengerId}/bookings/{bookingId}")
    public Booking getBooking(@RequestParam(name = "passengerId") Long passengerId,
                              @RequestParam(name = "bookingId") Long bookingId) {
        Passenger passenger = getPassengerFromId(passengerId);
        return getPassengerBookingFromId(bookingId, passenger);
    }

    @PostMapping("{passengerId}/bookings/")
    public void requestBooking(@RequestParam(name = "passengerId") Long passengerId,
                               @RequestBody Booking data) {
        Passenger passenger = getPassengerFromId(passengerId);
        Booking booking = Booking.builder()
                .build();
        bookingService.createBooking(booking);
        bookingRepository.save(booking);
        passengerRepository.save(passenger);
    }

    @DeleteMapping("{passengerId}/bookings/{bookingId}")
    public void cancelBooking(@RequestParam(name = "passengerId") Long passengerId,
                              @RequestParam(name = "bookingId") Long bookingId) {
        Passenger passenger = getPassengerFromId(passengerId);
        Booking booking = getPassengerBookingFromId(bookingId, passenger);
        bookingService.cancelByPassenger(passenger, booking);
    }

    // rate the booking
    // start the ride
    // end the ride

    @PatchMapping("{passengerId}/bookings/{bookingId}/rate")
    public void rateRide(@RequestParam(name = "passengerId") Long passengerId,
                         @RequestParam(name = "bookingId") Long bookingId,
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
