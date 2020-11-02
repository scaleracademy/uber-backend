package com.uber.uberapi.services;

import com.uber.uberapi.models.Booking;
import com.uber.uberapi.models.DateUtils;
import com.uber.uberapi.repositories.BookingRepository;
import com.uber.uberapi.services.messagequeue.MQMessage;
import com.uber.uberapi.services.messagequeue.MessageQueue;
import com.uber.uberapi.services.notification.NotificationService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Service
public class SchedulingService {
    @Autowired
    MessageQueue messageQueue;
    @Autowired
    Constants constants;
    @Autowired
    LocationTrackingService locationTrackingService;
    @Autowired
    NotificationService notificationService;
    @Autowired
    BookingRepository bookingRepository;
    @Autowired
    BookingService bookingService;

    Set<Booking> scheduledBookings = new HashSet<>();

    @Scheduled(fixedRate = 1000)
    public void consumer() {
        MQMessage m = messageQueue.consumeMessage(constants.getSchedulingTopicName());
        if (m == null) return;
        Message message = (Message) m;
        schedule(message.getBooking());
    }

    public void schedule(Booking booking) {
        scheduledBookings.add(booking);
    }

    @Scheduled(fixedRate = 60000)
    public void process() {
        Set<Booking> newScheduledBookings = new HashSet<>();
        for (Booking booking : scheduledBookings) {
            if (DateUtils.addMinutes(new Date(), constants.getBookingProcessBeforeTime()).after(booking.getScheduledTime())) {
                bookingService.acceptBooking(booking.getDriver(), booking);
            } else {
                newScheduledBookings.add(booking);
            }
        }
        scheduledBookings = newScheduledBookings;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Message implements MQMessage {
        private Booking booking;
    }
}

// PubSub design pattern

// Observer Design Pattern -> events
