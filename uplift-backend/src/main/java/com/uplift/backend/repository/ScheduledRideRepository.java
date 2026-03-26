package com.uplift.backend.repository;

import com.uplift.backend.entity.ScheduledRide;
import com.uplift.backend.entity.User;
import com.uplift.backend.enums.ScheduledRideStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ScheduledRideRepository extends JpaRepository<ScheduledRide, Long> {

    // existing methods...

    List<ScheduledRide> findByRiderAndDateTimeAfterOrderByDateTimeAsc(
            User rider,
            LocalDateTime after
    );

    List<ScheduledRide> findByRiderAndDateTimeBeforeOrderByDateTimeDesc(
            User rider,
            LocalDateTime before
    );
    List<ScheduledRide> findByFromLocationAndToLocationAndDateTimeBetweenAndStatus(
            String fromLocation,
            String toLocation,
            LocalDateTime start,
            LocalDateTime end,
            ScheduledRideStatus status
    );
    long countByRiderAndDateTimeBetween(User rider, LocalDateTime start, LocalDateTime end);

    // 🔹 For rider dashboard stats (upcoming scheduled)
    long countByRiderAndStatus(User rider, ScheduledRideStatus status);

    // 🔹 For rider’s scheduled rides list
    List<ScheduledRide> findByRider(User rider);
}
