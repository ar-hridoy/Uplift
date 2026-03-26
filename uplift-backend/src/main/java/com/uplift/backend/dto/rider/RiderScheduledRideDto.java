package com.uplift.backend.dto.rider;

import com.uplift.backend.enums.ScheduledRideStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class RiderScheduledRideDto {

    private Long id;
    private String pickupLocation;
    private String dropoffLocation;
    private LocalDateTime dateTime;   // departure time
    private int totalSeats;
    private int bookedSeats;
    private BigDecimal pricePerSeat;
    private ScheduledRideStatus status;

    public RiderScheduledRideDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPickupLocation() {
        return pickupLocation;
    }

    public void setPickupLocation(String pickupLocation) {
        this.pickupLocation = pickupLocation;
    }

    public String getDropoffLocation() {
        return dropoffLocation;
    }

    public void setDropoffLocation(String dropoffLocation) {
        this.dropoffLocation = dropoffLocation;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public int getTotalSeats() {
        return totalSeats;
    }

    public void setTotalSeats(int totalSeats) {
        this.totalSeats = totalSeats;
    }

    public int getBookedSeats() {
        return bookedSeats;
    }

    public void setBookedSeats(int bookedSeats) {
        this.bookedSeats = bookedSeats;
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
}
