package com.uber.uberapi.repositories;

import com.uber.uberapi.models.PaymentReceipt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentReceiptRepository extends JpaRepository<PaymentReceipt, Long> {
}
