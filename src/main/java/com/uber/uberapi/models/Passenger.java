package com.uber.uberapi.models;

import lombok.*;

import javax.persistence.*;
import java.awt.print.Book;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "passenger")
public class Passenger extends Auditable {
    @OneToOne(cascade = CascadeType.ALL)
    private Account account;

    private String name;

    @Enumerated(value = EnumType.STRING)
    private Gender gender;

    @OneToMany(mappedBy = "passenger")
    private List<Booking> bookings = new ArrayList<>();

    @OneToOne
    private Booking activeBooking = null;

    @Temporal(value = TemporalType.DATE)
    private Date dob;

    private String phoneNumber;

    @OneToOne
    private ExactLocation home;
    @OneToOne
    private ExactLocation work;
    @OneToOne
    private ExactLocation lastKnownLocation;

    @OneToOne
    private Review avgRating;
    // this is updated by a cron job that runs nightly
}