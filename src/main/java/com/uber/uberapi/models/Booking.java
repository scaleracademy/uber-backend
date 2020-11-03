package com.uber.uberapi.models;

import com.uber.uberapi.exceptions.InvalidActionForBookingStateException;
import com.uber.uberapi.exceptions.InvalidOTPException;
import lombok.*;

import javax.persistence.*;
import java.util.*;


@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "booking", indexes = {
        @Index(columnList = "passenger_id"),
        @Index(columnList = "driver_id"),
})
public class Booking extends Auditable {
    @ManyToOne
    private Passenger passenger;

    @ManyToOne
    private Driver driver;

    @ManyToMany(cascade = CascadeType.PERSIST)
    private Set<Driver> notifiedDrivers = new HashSet<>(); // store which drivers can potentially accept this booking

    @Enumerated(value = EnumType.STRING)
    private BookingType bookingType;

    @Enumerated(value = EnumType.STRING)
    private BookingStatus bookingStatus;

    @OneToOne
    private Review reviewByPassenger;
    @OneToOne
    private Review reviewByDriver;

    @OneToOne
    private PaymentReceipt paymentReceipt; // todo: add payment services

    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "booking_route",
            joinColumns = @JoinColumn(name = "booking_id"),
            inverseJoinColumns = @JoinColumn(name = "exact_location_id"),
            indexes = {@Index(columnList = "booking_id")}
    )
    @OrderColumn(name = "location_index")
    private List<ExactLocation> route = new ArrayList<>();
    // every booking has a list of locations (route)
    // one to many mapping

    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "booking_completed_route",
            joinColumns = @JoinColumn(name = "booking_id"),
            inverseJoinColumns = @JoinColumn(name = "exact_location_id"),
            indexes = {@Index(columnList = "booking_id")}
    )
    @OrderColumn(name = "location_index")
    private List<ExactLocation> completedRoute = new ArrayList<>();

    @Temporal(value = TemporalType.TIMESTAMP)
    private Date scheduledTime;

    @Temporal(value = TemporalType.TIMESTAMP)
    private Date startTime; // actual start time

    @Temporal(value = TemporalType.TIMESTAMP)
    private Date endTime; // actual end time

    @Temporal(value = TemporalType.TIMESTAMP)
    private Date expectedCompletionTime; // filled by the location Tracking service

    private Long totalDistanceMeters; // also be tracked by the location tracking service

    @OneToOne
    private OTP rideStartOTP; // a cron job deleted otps of completed rides

    public void startRide(OTP otp, int rideStartOTPExpiryMinutes) {
        if (!bookingStatus.equals(BookingStatus.CAB_ARRIVED)) {
            throw new InvalidActionForBookingStateException("Cannot start the ride before the driver has reached the pickup point");
        }
        if (!rideStartOTP.validateEnteredOTP(otp, rideStartOTPExpiryMinutes))
            throw new InvalidOTPException();
        startTime = new Date();
        passenger.setActiveBooking(this);
        bookingStatus = BookingStatus.IN_RIDE;
    }

    public void endRide() {
        if (!bookingStatus.equals(BookingStatus.IN_RIDE)) {
            throw new InvalidActionForBookingStateException("The ride hasn't started yet");
        }
        driver.setActiveBooking(null);
        endTime = new Date();
        passenger.setActiveBooking(null);
        bookingStatus = BookingStatus.COMPLETED;
    }

    public boolean canChangeRoute() {
        return bookingStatus.equals(BookingStatus.ASSIGNING_DRIVER)
                || bookingStatus.equals(BookingStatus.CAB_ARRIVED)
                || bookingStatus.equals(BookingStatus.IN_RIDE)
                || bookingStatus.equals(BookingStatus.SCHEDULED)
                || bookingStatus.equals(BookingStatus.REACHING_PICKUP_LOCATION);
    }

    public boolean needsDriver() {
        return bookingStatus.equals(BookingStatus.ASSIGNING_DRIVER);
    }

    public ExactLocation getPickupLocation() {
        return route.get(0);
    }

    public void cancel() {
        if (!(bookingStatus.equals(BookingStatus.REACHING_PICKUP_LOCATION)
                || bookingStatus.equals(BookingStatus.ASSIGNING_DRIVER)
                || bookingStatus.equals(BookingStatus.SCHEDULED)
                || bookingStatus.equals(BookingStatus.CAB_ARRIVED))) {
            throw new InvalidActionForBookingStateException("Cannot cancel the booking now.");
        }
        bookingStatus = BookingStatus.CANCELLED;
        driver = null;
        notifiedDrivers.clear();
    }
}
