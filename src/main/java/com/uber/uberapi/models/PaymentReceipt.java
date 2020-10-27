package com.uber.uberapi.models;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="paymentreceipt")
public class PaymentReceipt extends Auditable {
    private Double amount;
    @ManyToOne
    private PaymentGateway paymentGateway;
    private String details;
}
