package com.uplift.backend.entity;

import com.uplift.backend.enums.SeatBookingStatus;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "scheduled_seat_bookings")
public class SeatBooking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private ScheduledRide scheduledRide;

    @ManyToOne(optional = false)
    private User passenger;

    private int seats;
    private BigDecimal totalPrice;

    @Enumerated(EnumType.STRING)
    private SeatBookingStatus status = SeatBookingStatus.PENDING;

    private LocalDateTime createdAt = LocalDateTime.now();

    // ====== Getters & Setters ======

    public Long getId() {
        return id;
    }

    public ScheduledRide getScheduledRide() {
        return scheduledRide;
    }

    public void setScheduledRide(ScheduledRide scheduledRide) {
        this.scheduledRide = scheduledRide;
    }

    public User getPassenger() {
        return passenger;
    }

    public void setPassenger(User passenger) {
        this.passenger = passenger;
    }

    public int getSeats() {
        return seats;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public SeatBookingStatus getStatus() {
        return status;
    }

    public void setStatus(SeatBookingStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
