package com.uplift.backend.service;

import com.uplift.backend.dto.ride.InstantRideRequestDto;
import com.uplift.backend.entity.InstantRide;
import com.uplift.backend.entity.User;
import com.uplift.backend.enums.InstantRideStatus;
import com.uplift.backend.repository.InstantRideRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RideService {

    private final InstantRideRepository instantRideRepository;

    public RideService(InstantRideRepository instantRideRepository) {
        this.instantRideRepository = instantRideRepository;
    }

    public InstantRide createInstantRide(User passenger, InstantRideRequestDto dto) {
        InstantRide ride = new InstantRide();
        ride.setPassenger(passenger);
        ride.setStartLocation(dto.getFromLocation());
        ride.setEndLocation(dto.getToLocation());
        ride.setEstimatedFare(dto.getEstimatedFare());
        ride.setStatus(InstantRideStatus.REQUESTED);
        return instantRideRepository.save(ride);
    }

    public List<InstantRide> getPendingInstantRides() {
        return instantRideRepository.findByStatus(InstantRideStatus.REQUESTED);
    }

    public InstantRide acceptInstantRide(User rider, Long rideId) {
        InstantRide ride = instantRideRepository.findById(rideId)
                .orElseThrow(() -> new RuntimeException("Ride not found"));
        ride.setRider(rider);
        ride.setStatus(InstantRideStatus.ACCEPTED);
        return instantRideRepository.save(ride);
    }

    // More methods: startRide, completeRide, cancelRide, etc.
}
