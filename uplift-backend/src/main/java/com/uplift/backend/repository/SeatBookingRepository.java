package com.uplift.backend.repository;

import com.uplift.backend.entity.ScheduledRide;
import com.uplift.backend.entity.SeatBooking;
import com.uplift.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SeatBookingRepository extends JpaRepository<SeatBooking, Long> {

    // 🔹 Used in ScheduledRideService to get all bookings of a ride
    List<SeatBooking> findByScheduledRide(ScheduledRide scheduledRide);

    // 🔹 Already used somewhere else, keep it:
    int countByScheduledRide(ScheduledRide scheduledRide);
    List<SeatBooking> findByPassenger(User passenger);
}
