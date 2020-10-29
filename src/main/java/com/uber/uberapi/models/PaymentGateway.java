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
@Table(name = "paymentgateway")
public class PaymentGateway extends Auditable {
    private String name;
}
