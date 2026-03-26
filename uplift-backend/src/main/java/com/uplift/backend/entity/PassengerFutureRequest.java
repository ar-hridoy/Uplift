package com.uplift.backend.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "passenger_future_requests")
public class PassengerFutureRequest {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private User passenger;

    private LocalDateTime dateTime;
    private String fromLocation;
    private String toLocation;
    private BigDecimal preferredPrice;
    private String status = "OPEN"; // can use enum if you want

    private LocalDateTime createdAt = LocalDateTime.now();

    // getters/setters
}
