package com.uplift.backend.entity;

import com.uplift.backend.enums.ScheduledRideStatus;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "scheduled_rides")
public class ScheduledRide {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private User rider;

    @ManyToOne(optional = false)
    private Vehicle vehicle;

    private LocalDateTime dateTime;

    private String fromLocation;
    private String toLocation;

    private int totalSeats;

    // ⚠️ NEW: seatsLeft field
    @Column(nullable = false)
    private int seatsLeft;

    private BigDecimal pricePerSeat;

    @Enumerated(EnumType.STRING)
    private ScheduledRideStatus status = ScheduledRideStatus.OPEN;

    private LocalDateTime createdAt = LocalDateTime.now();

    // ====== Getters & Setters ======

    public Long getId() {
        return id;
    }

    public User getRider() {
        return rider;
    }

    public void setRider(User rider) {
        this.rider = rider;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getFromLocation() {
        return fromLocation;
    }

    public void setFromLocation(String fromLocation) {
        this.fromLocation = fromLocation;
    }

    public String getToLocation() {
        return toLocation;
    }

    public void setToLocation(String toLocation) {
        this.toLocation = toLocation;
    }

    public int getTotalSeats() {
        return totalSeats;
    }

    public void setTotalSeats(int totalSeats) {
        this.totalSeats = totalSeats;
    }

    public int getSeatsLeft() {          // ✅ getter
        return seatsLeft;
    }

    public void setSeatsLeft(int seatsLeft) {   // ✅ setter
        this.seatsLeft = seatsLeft;
    }

    public BigDecimal getPricePerSeat() {
        return pricePerSeat;
    }

    public void setPricePerSeat(BigDecimal pricePerSeat) {
        this.pricePerSeat = pricePerSeat;
    }

    public ScheduledRideStatus getStatus() {
        return status;
    }

    public void setStatus(ScheduledRideStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
