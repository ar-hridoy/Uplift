package com.uplift.backend.controller;

import com.uplift.backend.entity.InstantRide;
import com.uplift.backend.entity.User;
import com.uplift.backend.repository.InstantRideRepository;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rides")
public class RideController {

    private final InstantRideRepository instantRideRepository;

    public RideController(InstantRideRepository instantRideRepository) {
        this.instantRideRepository = instantRideRepository;
    }

    // Passenger: my instant rides
    @GetMapping("/instant/passenger")
    public List<InstantRide> myInstantAsPassenger(@AuthenticationPrincipal User user) {
        return instantRideRepository.findByPassenger_Id(user.getId());
    }

    // Rider: my instant rides
    @GetMapping("/instant/rider")
    public List<InstantRide> myInstantAsRider(@AuthenticationPrincipal User user) {
        return instantRideRepository.findByRider_Id(user.getId());
    }

    // Admin: all instant rides
    @GetMapping("/instant")
    public List<InstantRide> allInstant() {
        return instantRideRepository.findAll();
    }
}
