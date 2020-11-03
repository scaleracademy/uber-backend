package com.uber.uberapi.services.drivermatching;

import com.uber.uberapi.models.Booking;
import com.uber.uberapi.models.Driver;
import com.uber.uberapi.models.ExactLocation;
import com.uber.uberapi.repositories.BookingRepository;
import com.uber.uberapi.services.Constants;
import com.uber.uberapi.services.ETAService;
import com.uber.uberapi.services.drivermatching.filters.DriverFilter;
import com.uber.uberapi.services.drivermatching.filters.ETABasedFilter;
import com.uber.uberapi.services.drivermatching.filters.GenderFilter;
import com.uber.uberapi.services.locationtracking.LocationTrackingService;
import com.uber.uberapi.services.messagequeue.MQMessage;
import com.uber.uberapi.services.messagequeue.MessageQueue;
import com.uber.uberapi.services.notification.NotificationService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DriverMatchingService {
    final MessageQueue messageQueue;
    final Constants constants;
    final LocationTrackingService locationTrackingService;
    final NotificationService notificationService;
    final BookingRepository bookingRepository;
    final ETAService etaService;

    final List<DriverFilter> driverFilters = new ArrayList<>();

    public DriverMatchingService(MessageQueue messageQueue, Constants constants, LocationTrackingService locationTrackingService, NotificationService notificationService, BookingRepository bookingRepository, ETAService etaService) {
        this.messageQueue = messageQueue;
        this.constants = constants;
        this.locationTrackingService = locationTrackingService;
        this.notificationService = notificationService;
        this.bookingRepository = bookingRepository;
        this.etaService = etaService;

        driverFilters.add(new ETABasedFilter(this.etaService, constants));
        driverFilters.add(new GenderFilter(constants));
    }

    @Scheduled(fixedRate = 1000)
    public void consumer() {
        MQMessage m = messageQueue.consumeMessage(constants.getDriverMatchingTopicName());
        if (m == null) return;
        Message message = (Message) m;
        findNearbyDrivers(message.getBooking());
    }

    private void findNearbyDrivers(Booking booking) {
        ExactLocation pickup = booking.getPickupLocation();
        List<Driver> drivers = locationTrackingService.getDriversNearLocation(pickup);
        if (drivers.size() == 0) {
            // todo: add surge fee and send notifications to nearby drivers about the surge
            notificationService.notify(booking.getPassenger().getPhoneNumber(), "No cabs near you");
            return;
        }
        notificationService.notify(booking.getPassenger().getPhoneNumber(),
                String.format("Contacting %s cabs around you", drivers.size()));

        drivers = filterDrivers(drivers, booking);

        if (drivers.size() == 0) {
            notificationService.notify(booking.getPassenger().getPhoneNumber(), "No cabs near you");
        }
        drivers.forEach(driver -> {
            notificationService.notify(driver.getPhoneNumber(), "Booking near you: " + booking.toString());
            driver.getAcceptableBookings().add(booking);
        });
        bookingRepository.save(booking);
    }

    private List<Driver> filterDrivers(List<Driver> drivers, Booking booking) {
        // todo: Chain of Responsibility pattern
        for (DriverFilter filter : driverFilters) {
            drivers = filter.apply(drivers, booking);
        }
        return drivers;
    }


    @AllArgsConstructor
    @Getter
    @Setter
    public static class Message implements MQMessage {
        private Booking booking;

        @Override
        public String toString() {
            return String.format("Need to find drivers for %s", booking.toString());
        }
    }
}
