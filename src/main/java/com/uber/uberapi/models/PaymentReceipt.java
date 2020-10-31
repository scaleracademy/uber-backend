package com.uber.uberapi.models;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

// why an index?

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "paymentreceipt", indexes = {
        @Index(columnList = "payment_gateway_id")
})
public class PaymentReceipt extends Auditable {
    private Double amount;
    @ManyToOne
    private PaymentGateway paymentGateway;
    private String details;
}
