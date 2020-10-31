package com.uber.uberapi.models;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "paymentgateway")
public class PaymentGateway extends Auditable {
    private String name;

    @OneToMany(mappedBy = "paymentGateway")
    private Set<PaymentReceipt> receipts = new HashSet<>();
}
