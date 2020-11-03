package com.uber.uberapi.models;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "namedlocation")
public class NamedLocation extends Auditable {
    // search/autocomplete service

    @OneToOne
    private ExactLocation exactLocation;

    private String name;
    private String zipCode;
    private String city;
    private String country;
    private String state;
}
