package com.uplift.backend.repository;

import com.uplift.backend.entity.InstantRide;
import com.uplift.backend.entity.User;
import com.uplift.backend.enums.InstantRideStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface InstantRideRepository extends JpaRepository<InstantRide, Long> {

    long countByRiderAndCreatedAtBetween(User rider, LocalDateTime start, LocalDateTime end);

    long countByRiderAndStatus(User rider, InstantRideStatus status);

    List<InstantRide> findTop20ByRiderOrderByCreatedAtDesc(User rider);

    List<InstantRide> findByRider(User rider);

    List<InstantRide> findByPassenger_Id(Long passengerId);

    List<InstantRide> findByRider_Id(Long riderId);

    // ✅ Add this missing method
    List<InstantRide> findByStatus(InstantRideStatus status);

}
