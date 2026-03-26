package com.uplift.backend.service;

import com.uplift.backend.dto.rider.RiderInstantRideDto;
import com.uplift.backend.entity.InstantRide;
import com.uplift.backend.enums.InstantRideStatus;
import com.uplift.backend.entity.User;
import com.uplift.backend.repository.InstantRideRepository;
import com.uplift.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RiderInstantRideService {

    private final InstantRideRepository instantRideRepository;
    private final UserRepository userRepository;

    public RiderInstantRideService(InstantRideRepository instantRideRepository,
                                   UserRepository userRepository) {
        this.instantRideRepository = instantRideRepository;
        this.userRepository = userRepository;
    }

    // Rider fetches pending instant ride requests
    public List<RiderInstantRideDto> getPendingInstantRides() {
        List<InstantRide> rides = instantRideRepository.findByStatus(InstantRideStatus.REQUESTED);

        return rides.stream().map(r -> {
            RiderInstantRideDto dto = new RiderInstantRideDto();
            dto.setRideId(r.getId());
            dto.setPassengerName(r.getPassenger().getFullName());
            dto.setPickup(r.getStartLocation());
            dto.setDropoff(r.getEndLocation());
            dto.setEstimatedFare(r.getEstimatedFare());
            dto.setEstimatedDistanceKm(r.getDistanceKm());
            dto.setRequestedAt(r.getCreatedAt());
            return dto;
        }).collect(Collectors.toList());
    }

    // Rider accepts a ride
    public boolean acceptRide(Long rideId, Long riderId) {
        InstantRide ride = instantRideRepository.findById(rideId).orElse(null);
        if (ride == null) return false;

        User rider = userRepository.findById(riderId).orElse(null);
        if (rider == null) return false;

        if (ride.getStatus() != InstantRideStatus.REQUESTED) return false;

        ride.setRider(rider);
        ride.setStatus(InstantRideStatus.ACCEPTED);
        ride.setCreatedAt(java.time.LocalDateTime.now());
        instantRideRepository.save(ride);

        return true;
    }
}
