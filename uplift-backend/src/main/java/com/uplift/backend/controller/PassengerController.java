package com.uplift.backend.controller;

import com.uplift.backend.dto.ride.InstantRideRequestDto;
import com.uplift.backend.dto.ride.SeatBookingCreateDto;
import com.uplift.backend.entity.InstantRide;
import com.uplift.backend.entity.SeatBooking;
import com.uplift.backend.entity.ScheduledRide;
import com.uplift.backend.entity.User;
import com.uplift.backend.service.RideService;
import com.uplift.backend.service.ScheduledRideService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/passenger")
public class PassengerController {

    private final RideService rideService;
    private final ScheduledRideService scheduledRideService;

    public PassengerController(RideService rideService,
                               ScheduledRideService scheduledRideService) {
        this.rideService = rideService;
        this.scheduledRideService = scheduledRideService;
    }

    @PostMapping("/instant-rides")
    public InstantRide createInstantRide(@AuthenticationPrincipal User passenger,
                                         @Valid @RequestBody InstantRideRequestDto dto) {
        return rideService.createInstantRide(passenger, dto);
    }

    @GetMapping("/scheduled-rides/search")
    public List<ScheduledRide> searchScheduled(@RequestParam String from,
                                               @RequestParam String to,
                                               @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                               LocalDate date) {
        return scheduledRideService.search(from, to, date);
    }

    // Seat booking endpoint will use SeatBookingService when you add it
    @PostMapping("/scheduled-rides/{id}/book")
    public SeatBooking bookSeats(@PathVariable Long id,
                                 @AuthenticationPrincipal User passenger,
                                 @RequestBody SeatBookingCreateDto dto) {
        // TODO: implement SeatBookingService and call here
        return null;
    }
}
