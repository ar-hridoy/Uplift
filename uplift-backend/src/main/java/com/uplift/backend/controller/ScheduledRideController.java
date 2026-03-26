package com.uplift.backend.controller;

import com.uplift.backend.dto.ride.SeatBookingCreateDto;
import com.uplift.backend.entity.SeatBooking;
import com.uplift.backend.entity.ScheduledRide;
import com.uplift.backend.entity.User;
import com.uplift.backend.repository.SeatBookingRepository;
import com.uplift.backend.repository.ScheduledRideRepository;
import com.uplift.backend.service.ScheduledRideService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/scheduled-rides")
public class ScheduledRideController {

    private final ScheduledRideService scheduledRideService;
    private final ScheduledRideRepository scheduledRideRepository;
    private final SeatBookingRepository seatBookingRepository;

    public ScheduledRideController(ScheduledRideService scheduledRideService,
                                   ScheduledRideRepository scheduledRideRepository,
                                   SeatBookingRepository seatBookingRepository) {
        this.scheduledRideService = scheduledRideService;
        this.scheduledRideRepository = scheduledRideRepository;
        this.seatBookingRepository = seatBookingRepository;
    }

    // Passenger: book seats
    @PostMapping("/{rideId}/book")
    public SeatBooking bookSeats(@PathVariable Long rideId,
                                 @AuthenticationPrincipal User passenger,
                                 @Valid @RequestBody SeatBookingCreateDto dto) {
        return scheduledRideService.bookSeats(rideId, passenger, dto.getSeats());
    }

    // Rider: view bookings for a scheduled ride
    @GetMapping("/{rideId}/bookings")
    public List<SeatBooking> bookingsForRide(@PathVariable Long rideId) {
        ScheduledRide ride = scheduledRideRepository.findById(rideId)
                .orElseThrow(() -> new RuntimeException("Scheduled ride not found"));

        return seatBookingRepository.findByScheduledRide(ride);
    }

    // Passenger: my bookings
    @GetMapping("/my-bookings")
    public List<SeatBooking> myBookings(@AuthenticationPrincipal User passenger) {
        return seatBookingRepository.findByPassenger(passenger);
    }
}
