package com.uplift.backend.service;

import com.uplift.backend.dto.ride.ScheduledRideCreateDto;
import com.uplift.backend.entity.SeatBooking;
import com.uplift.backend.entity.ScheduledRide;
import com.uplift.backend.entity.User;
import com.uplift.backend.entity.Vehicle;
import com.uplift.backend.enums.ScheduledRideStatus;
import com.uplift.backend.repository.SeatBookingRepository;
import com.uplift.backend.repository.ScheduledRideRepository;
import com.uplift.backend.repository.VehicleRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ScheduledRideService {

    private final ScheduledRideRepository scheduledRideRepository;
    private final VehicleRepository vehicleRepository;
    private final SeatBookingRepository seatBookingRepository;

    public ScheduledRideService(ScheduledRideRepository scheduledRideRepository,
                                VehicleRepository vehicleRepository,
                                SeatBookingRepository seatBookingRepository) {
        this.scheduledRideRepository = scheduledRideRepository;
        this.vehicleRepository = vehicleRepository;
        this.seatBookingRepository = seatBookingRepository;
    }

    /**
     * Rider creates a scheduled ride (post a ride).
     */
    public ScheduledRide createByRider(User rider, ScheduledRideCreateDto dto) {
        // find vehicle
        Vehicle vehicle = vehicleRepository.findById(dto.getVehicleId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Vehicle not found"
                ));

        ScheduledRide ride = new ScheduledRide();
        ride.setRider(rider);
        ride.setVehicle(vehicle);
        ride.setFromLocation(dto.getFromLocation());
        ride.setToLocation(dto.getToLocation());
        ride.setDateTime(dto.getDateTime());   // LocalDateTime
        ride.setTotalSeats(dto.getTotalSeats());
        ride.setPricePerSeat(dto.getPricePerSeat());
        ride.setStatus(ScheduledRideStatus.OPEN);
        ride.setCreatedAt(LocalDateTime.now());

        return scheduledRideRepository.save(ride);
    }

    /**
     * Passenger searches rides by from/to/date.
     */
    public List<ScheduledRide> search(String from, String to, LocalDate date) {
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.plusDays(1).atStartOfDay();

        return scheduledRideRepository
                .findByFromLocationAndToLocationAndDateTimeBetweenAndStatus(
                        from,
                        to,
                        start,
                        end,
                        ScheduledRideStatus.OPEN
                );
    }

    /**
     * Passenger books seats on a scheduled ride.
     */
    public SeatBooking bookSeats(Long rideId, User passenger, int seats) {
        ScheduledRide ride = scheduledRideRepository.findById(rideId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Ride not found"
                ));

        // How many seats already booked on this ride
        int bookedSeats = seatBookingRepository.findByScheduledRide(ride)
                .stream()
                .mapToInt(SeatBooking::getSeats)
                .sum();

        if (bookedSeats + seats > ride.getTotalSeats()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Not enough seats available"
            );
        }

        SeatBooking booking = new SeatBooking();
        booking.setScheduledRide(ride);
        booking.setPassenger(passenger);
        booking.setSeats(seats);
        booking.setTotalPrice(
                ride.getPricePerSeat().multiply(java.math.BigDecimal.valueOf(seats))
        );

        return seatBookingRepository.save(booking);
    }
}
