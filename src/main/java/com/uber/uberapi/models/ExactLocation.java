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
    private String latitude;
    private String longitude;
}

// multiple entries in our table
// with the same lat, long

