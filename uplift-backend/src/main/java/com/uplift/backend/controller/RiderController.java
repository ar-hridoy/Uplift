package com.uplift.backend.controller;

import com.uplift.backend.entity.InstantRide;
import com.uplift.backend.entity.ScheduledRide;
import com.uplift.backend.entity.Payment;
import com.uplift.backend.entity.User;

import com.uplift.backend.enums.InstantRideStatus;
import com.uplift.backend.enums.ScheduledRideStatus;

import com.uplift.backend.repository.InstantRideRepository;
import com.uplift.backend.repository.ScheduledRideRepository;
import com.uplift.backend.repository.PaymentRepository;

import com.uplift.backend.dto.rider.RiderInstantRideDto;
import com.uplift.backend.dto.rider.RiderScheduledRideDto;

import com.uplift.backend.dto.ride.ScheduledRideCreateDto;

import com.uplift.backend.service.RiderInstantRideService;
import com.uplift.backend.service.RiderScheduledRideService;
import com.uplift.backend.service.ScheduledRideService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/rider")
public class RiderController {

    private final InstantRideRepository instantRideRepository;
    private final ScheduledRideRepository scheduledRideRepository;
    private final PaymentRepository paymentRepository;
    private final RiderInstantRideService riderInstantRideService;
    private final RiderScheduledRideService riderScheduledRideService;
    private final ScheduledRideService scheduledRideService;

    public RiderController(InstantRideRepository instantRideRepository,
                           ScheduledRideRepository scheduledRideRepository,
                           PaymentRepository paymentRepository,
                           RiderInstantRideService riderInstantRideService,
                           RiderScheduledRideService riderScheduledRideService,
                           ScheduledRideService scheduledRideService) {

        this.instantRideRepository = instantRideRepository;
        this.scheduledRideRepository = scheduledRideRepository;
        this.paymentRepository = paymentRepository;
        this.riderInstantRideService = riderInstantRideService;
        this.riderScheduledRideService = riderScheduledRideService;
        this.scheduledRideService = scheduledRideService;
    }

    // ================================
    // CREATE SCHEDULED RIDE BY RIDER
    // ================================
    @PostMapping("/scheduled/create")
    public ResponseEntity<?> createScheduledRide(
            @AuthenticationPrincipal User rider,
            @RequestBody ScheduledRideCreateDto dto
    ) {
        if (dto.getDateTime() == null ||
                dto.getFromLocation() == null ||
                dto.getToLocation() == null) {
            return ResponseEntity.badRequest().body("Missing required fields");
        }

        ScheduledRide ride = scheduledRideService.createByRider(rider, dto);
        return ResponseEntity.ok(ride);
    }

    // ================================
    // SCHEDULED RIDES LISTING
    // ================================
    @GetMapping("/scheduled/upcoming")
    public ResponseEntity<List<RiderScheduledRideDto>> getUpcomingScheduledRides(
            @AuthenticationPrincipal User rider) {
        return ResponseEntity.ok(riderScheduledRideService.getUpcomingRides(rider));
    }

    @GetMapping("/scheduled/history")
    public ResponseEntity<List<RiderScheduledRideDto>> getPastScheduledRides(
            @AuthenticationPrincipal User rider) {
        return ResponseEntity.ok(riderScheduledRideService.getPastRides(rider));
    }

    // ================================
    // INSTANT RIDES
    // ================================
    @GetMapping("/instant/pending")
    public ResponseEntity<List<RiderInstantRideDto>> getPendingInstantRides() {
        return ResponseEntity.ok(riderInstantRideService.getPendingInstantRides());
    }

    @PostMapping("/instant/accept/{rideId}")
    public ResponseEntity<String> acceptRide(
            @PathVariable Long rideId,
            @RequestParam Long riderId) {

        boolean ok = riderInstantRideService.acceptRide(rideId, riderId);
        return ok
                ? ResponseEntity.ok("Ride Accepted")
                : ResponseEntity.badRequest().body("Cannot accept ride");
    }

    // ================================
    // DASHBOARD SUMMARY
    // ================================
    @GetMapping("/dashboard-summary")
    public Map<String, Object> dashboardSummary(@AuthenticationPrincipal User rider) {

        Map<String, Object> map = new HashMap<>();

        LocalDate today = LocalDate.now();
        LocalDateTime start = today.atStartOfDay();
        LocalDateTime end = today.plusDays(1).atStartOfDay();

        long instantToday = instantRideRepository
                .countByRiderAndCreatedAtBetween(rider, start, end);

        long scheduledToday = scheduledRideRepository
                .countByRiderAndDateTimeBetween(rider, start, end);

        BigDecimal earningsToday = paymentRepository
                .sumByPayeeAndCreatedAtBetween(rider, start, end);
        if (earningsToday == null) earningsToday = BigDecimal.ZERO;

        long activeInstant = instantRideRepository
                .countByRiderAndStatus(rider, InstantRideStatus.ACCEPTED);

        long upcomingScheduled = scheduledRideRepository
                .countByRiderAndStatus(rider, ScheduledRideStatus.OPEN);

        map.put("todayRides", instantToday + scheduledToday);
        map.put("earningsToday", earningsToday);
        map.put("activeInstant", activeInstant);
        map.put("upcomingScheduled", upcomingScheduled);

        return map;
    }

    // ================================
    // RECENT RIDES
    // ================================
    @GetMapping("/recent-rides")
    public List<InstantRide> recentInstantRides(@AuthenticationPrincipal User rider) {
        return instantRideRepository.findTop20ByRiderOrderByCreatedAtDesc(rider);
    }

    // ================================
    // ALL RIDES
    // ================================
    @GetMapping("/instant-rides")
    public List<InstantRide> instantRides(@AuthenticationPrincipal User rider) {
        return instantRideRepository.findByRider(rider);
    }

    @GetMapping("/scheduled-rides")
    public List<ScheduledRide> scheduledRides(@AuthenticationPrincipal User rider) {
        return scheduledRideRepository.findByRider(rider);
    }

    // ================================
    // EARNINGS
    // ================================
    @GetMapping("/earnings-summary")
    public Map<String, Object> earningsSummary(@AuthenticationPrincipal User rider) {

        Map<String, Object> map = new HashMap<>();

        BigDecimal total = paymentRepository.sumByPayee(rider);
        if (total == null) total = BigDecimal.ZERO;

        LocalDateTime weekStart = LocalDate.now().minusDays(7).atStartOfDay();
        LocalDateTime now = LocalDateTime.now();

        BigDecimal week = paymentRepository.sumByPayeeAndCreatedAtBetween(rider, weekStart, now);
        if (week == null) week = BigDecimal.ZERO;

        map.put("totalEarnings", total);
        map.put("weekEarnings", week);
        map.put("pendingPayout", BigDecimal.ZERO);

        return map;
    }
}
