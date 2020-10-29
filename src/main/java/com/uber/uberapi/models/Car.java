package com.uber.uberapi.models;

import lombok.*;

import javax.persistence.*;


@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "car", indexes = {
        @Index(columnList = "driver_id", unique = true)
})
public class Car extends Auditable {
    @ManyToOne
    private Color color;

    private String plateNumber;

    private String brandAndModel;

    @Enumerated(value = EnumType.STRING)
    private CarType carType;

    @OneToOne
    private Driver driver;
}
