package com.uplift.backend.service;

import com.uplift.backend.dto.rider.RiderScheduledRideDto;
import com.uplift.backend.entity.ScheduledRide;
import com.uplift.backend.entity.User;
import com.uplift.backend.repository.ScheduledRideRepository;
import com.uplift.backend.repository.SeatBookingRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.time.LocalDateTime;

@Service
public class RiderScheduledRideService {

    private final ScheduledRideRepository scheduledRideRepository;
    private final SeatBookingRepository seatBookingRepository;

    public RiderScheduledRideService(ScheduledRideRepository scheduledRideRepository,
                                     SeatBookingRepository seatBookingRepository) {
        this.scheduledRideRepository = scheduledRideRepository;
        this.seatBookingRepository = seatBookingRepository;
    }

    public List<RiderScheduledRideDto> getUpcomingRides(User rider) {
        LocalDateTime now = LocalDateTime.now();
        return scheduledRideRepository
                .findByRiderAndDateTimeAfterOrderByDateTimeAsc(rider, now)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<RiderScheduledRideDto> getPastRides(User rider) {
        LocalDateTime now = LocalDateTime.now();
        return scheduledRideRepository
                .findByRiderAndDateTimeBeforeOrderByDateTimeDesc(rider, now)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private RiderScheduledRideDto toDto(ScheduledRide ride) {
        RiderScheduledRideDto dto = new RiderScheduledRideDto();
        dto.setId(ride.getId());

        // ⚠️ Adjust these getters to match your ScheduledRide entity:
        dto.setPickupLocation(ride.getFromLocation());   // or getFromLocation()
        dto.setDropoffLocation(ride.getToLocation()); // or getToLocation()
        dto.setDateTime(ride.getDateTime());               // LocalDateTime

        dto.setTotalSeats(ride.getTotalSeats());           // or getSeatCount() etc.
        dto.setBookedSeats(seatBookingRepository.countByScheduledRide(ride));
        dto.setPricePerSeat(ride.getPricePerSeat());       // BigDecimal
        dto.setStatus(ride.getStatus());

        return dto;
    }
}
