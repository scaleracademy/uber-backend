package com.uber.uberapi.models;

import com.uber.uberapi.exceptions.UnapprovedDriverException;
import com.uber.uberapi.utils.DateUtils;
import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

// Driver is a special account - not inheritance
// Composition - Driver has an account
// Passenger has an account
// modeled as inheritance
// table-per-class
// single-table
// no-parent-table
// join-table


@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "driver")
public class Driver extends Auditable {
    @OneToOne(cascade = CascadeType.ALL)
    private Account account;

    private String phoneNumber;

    private Gender gender;

    private String name;

    @OneToOne
    private Review avgRating; // will be updated by a nightly cron job

    @OneToOne(mappedBy = "driver", cascade = CascadeType.ALL)
    private Car car;

    private String licenseDetails;

    @Temporal(value = TemporalType.DATE)
    private Date dob;

    @Enumerated(value = EnumType.STRING)
    private DriverApprovalStatus approvalStatus;

    @OneToMany(mappedBy = "driver")
    private List<Booking> bookings; // bookings that the driver actually drove

    @ManyToMany(mappedBy = "notifiedDrivers", cascade = CascadeType.PERSIST)
    private Set<Booking> acceptableBookings = new HashSet<>(); // bookings that the driver can currently accept

    @OneToOne
    private Booking activeBooking = null;  // driver.active_booking_id  either be null or be a foreign key

    private Boolean isAvailable;

    private String activeCity;

    @OneToOne
    private ExactLocation lastKnownLocation;

    @OneToOne
    private ExactLocation home;

    public void setAvailable(Boolean available) {
        if (available && !approvalStatus.equals(DriverApprovalStatus.APPROVED)) {
            throw new UnapprovedDriverException("Driver approval pending or denied " + getId());
        }
        isAvailable = available;
    }

    public boolean canAcceptBooking(int maxWaitTimeForPreviousRide) {
        if (isAvailable && activeBooking == null) {
            return true;
        }
        return activeBooking.getExpectedCompletionTime().before(
                DateUtils.addMinutes(new Date(), maxWaitTimeForPreviousRide));
    }
}
