package com.uplift.backend.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "rider_offers")
public class RiderOffer {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private PassengerFutureRequest futureRequest;

    @ManyToOne(optional = false)
    private User rider;

    private BigDecimal offeredPrice;
    private String status = "PENDING"; // PENDING / ACCEPTED / REJECTED

    private LocalDateTime createdAt = LocalDateTime.now();

    // getters/setters
}
