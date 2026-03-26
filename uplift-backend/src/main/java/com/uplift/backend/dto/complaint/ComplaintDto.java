package com.uplift.backend.dto.complaint;

import jakarta.validation.constraints.NotBlank;

public class ComplaintDto {

    private String rideType; // "INSTANT" or "SCHEDULED" (optional)
    private Long rideId;     // optional (can be null)

    private Long againstUserId; // rider or passenger

    @NotBlank
    private String message;

    public String getRideType() {
        return rideType;
    }

    public void setRideType(String rideType) {
        this.rideType = rideType;
    }

    public Long getRideId() {
        return rideId;
    }

    public void setRideId(Long rideId) {
        this.rideId = rideId;
    }

    public Long getAgainstUserId() {
        return againstUserId;
    }

    public void setAgainstUserId(Long againstUserId) {
        this.againstUserId = againstUserId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
