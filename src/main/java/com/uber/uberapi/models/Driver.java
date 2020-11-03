package com.uber.uberapi.models;

import com.uber.uberapi.exceptions.UnapprovedDriverException;
import com.uber.uberapi.utils.DateUtils;
import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "driver")
public class Driver extends Auditable {
    @OneToOne
    private Account account;

    private String phoneNumber;

    private Gender gender;

    private String name;

    @OneToOne(mappedBy = "driver")
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
