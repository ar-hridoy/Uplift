package com.uplift.backend.repository;

import com.uplift.backend.entity.Payment;
import com.uplift.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    // For PaymentService.paymentsForUser(...)
    List<Payment> findByPayer(User payer);

    List<Payment> findByPayee(User payee);

    // For RiderController / earnings summary etc.
    @Query("select sum(p.amount) from Payment p where p.payee = :payee")
    BigDecimal sumByPayee(User payee);

    @Query("select sum(p.amount) from Payment p where p.payee = :payee and p.createdAt between :start and :end")
    BigDecimal sumByPayeeAndCreatedAtBetween(User payee, LocalDateTime start, LocalDateTime end);
}
