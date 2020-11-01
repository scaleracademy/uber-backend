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
@Table(name = "constant")
public class Constant extends Auditable {
    private String name;
    private String value;

    public Long getAsLong() {
        return Long.parseLong(value);
    }
}
