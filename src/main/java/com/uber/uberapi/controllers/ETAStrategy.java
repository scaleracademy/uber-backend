package com.uber.uberapi.controllers;

import com.uber.uberapi.models.Booking;
import com.uber.uberapi.models.BookingType;
import com.uber.uberapi.models.ExactLocation;
import lombok.Builder;

import java.util.List;

// singleton - springs - @Component
// builder, strategy, factory, chainofresponsibility

class SomeController {
    void createBooking() {
        Booking booking = Booking.builder().build();
        BookingContext bookingContext = BookingStrategyFactory.autoConfigure(booking);
        bookingContext.etaStrategy.getETAInMinutes();
    }
}

class CombinedPricingStrategy implements PricingStrategy {
    private static List<PriceDelta>  priceDeltaList;

    static  {
        priceDeltaList.add(new WeatherPriceDelta());
        priceDeltaList.add(new BookingTypePriceDelta());
    }

    @Override
    public Integer getPriceInRupees(Booking booking) {
        Integer price = 1;
        for(PriceDelta priceDelta: priceDeltaList) {
            price = priceDelta.apply(booking, price);
        }
        return price;
    }
}

interface PriceDelta {
    public Integer apply(Booking booking, Integer price);
}

class BookingTypePriceDelta implements PriceDelta {
    @Override
    public Integer apply(Booking booking, Integer price) {
        if (booking.getBookingType().equals(BookingType.Prime))
            return price * 2;
        return price;
    }
}

class WeatherPriceDelta implements PriceDelta {
    @Override
    public Integer apply(Booking booking, Integer price) {
        if (weatherAPI.getCurrentWeather(booking.getRoute().get(0)).isBad()) {
            return price * 5;
        }
        return price;
    }
}

// Chain of Responsibility Pattern
// Price
// booking-type
// number of stops in the booking
// whether surge pricing
//   - weather
//   - time of day
//   - current demand
// discount on festivals

@Builder
class BookingContext {
    PricingStrategy pricingStrategy;
    ETAStrategy etaStrategy;
} // configuration

class BookingStrategyFactory {
    public static BookingContext autoConfigure(Booking booking) {
        if (booking.getBookingType().equals(BookingType.Prime))
            return getPrimeStrategy();
        return getXLStrategy();
    }

    public BookingContext getPrimeStrategy() {
        return BookingContext.builder()
                .etaStrategy(new PrimeETAStrategy())
                .pricingStrategy(new PrimePricingStrategy())
                .build();
    }

    public BookingContext getXLStrategy() {
        return BookingContext.builder()
                .etaStrategy(new PrimeETAStrategy())
                .pricingStrategy(new XLPricingStrategy())
                .build();
    }
}


interface PricingStrategy {
    public Integer getPriceInRupees(Booking booking);
}

class PrimePricingStrategy implements PricingStrategy {
    @Override
    public Integer getPriceInRupees(Booking booking) {
        return null;
    }
}


class XLPricingStrategy implements PricingStrategy {
    @Override
    public Integer getPriceInRupees(Booking booking) {
        return null;
    }
}


public interface ETAStrategy {
    public Integer getETAInMinutes(ExactLocation start, ExactLocation end);
}

class SimpleETAStrategy implements ETAStrategy {
    @Override
    public Integer getETAInMinutes(ExactLocation start, ExactLocation end) {
        return 10;
    }
}

class PrimeETAStrategy implements ETAStrategy {
    @Override
    public Integer getETAInMinutes(ExactLocation start, ExactLocation end) {
        return 5;
    }
}

class BadWeatherETAStrategy implements ETAStrategy {

    @Override
    public Integer getETAInMinutes(ExactLocation start, ExactLocation end) {
        return 20;
    }
}