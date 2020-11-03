package com.uber.uberapi.models;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "exactlocation")
public class ExactLocation extends Auditable {
    private Double latitude;
    private Double longitude;

    public double distanceKm(ExactLocation other) {
        final Double R = 6371e3; // metres
        if ((latitude.equals(other.getLatitude())) && (longitude.equals(other.getLongitude()))) {
            return 0;
        }
        double theta = longitude - other.longitude;
        double dist = Math.sin(Math.toRadians(latitude)) * Math.sin(Math.toRadians(other.latitude)) + Math.cos(Math.toRadians(latitude)) * Math.cos(Math.toRadians(other.latitude)) * Math.cos(Math.toRadians(theta));
        return Math.toDegrees(Math.acos(dist)) * 60 * 1.85316;
    }
}

// Spatial Mapping
// Geo Location
// Library - Google, OpenMap
// Try out different projections

// multiple entries in our table
// with the same lat, long

