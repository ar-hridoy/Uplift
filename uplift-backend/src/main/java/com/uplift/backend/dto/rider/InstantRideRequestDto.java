package com.uplift.backend.dto.ride;

import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

public class InstantRideRequestDto {

    @NotBlank
    private String fromLocation;

    @NotBlank
    private String toLocation;

    // optional client-calculated estimate
    private BigDecimal estimatedFare;

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

    public BigDecimal getEstimatedFare() {
        return estimatedFare;
    }

    public void setEstimatedFare(BigDecimal estimatedFare) {
        this.estimatedFare = estimatedFare;
    }
}
