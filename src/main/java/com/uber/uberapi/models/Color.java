package com.uber.uberapi.models;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "color")
public class Color extends Auditable {
    @Column(unique = true, nullable = false)
    private String name;
}
