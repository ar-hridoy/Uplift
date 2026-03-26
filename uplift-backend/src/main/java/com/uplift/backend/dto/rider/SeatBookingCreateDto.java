package com.uplift.backend.dto.ride;

import jakarta.validation.constraints.Positive;

public class SeatBookingCreateDto {

    @Positive
    private int seats;

    public int getSeats() {
        return seats;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }
}
