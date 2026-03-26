package com.uplift.backend.repository;

import com.uplift.backend.entity.PassengerFutureRequest;
import com.uplift.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PassengerFutureRequestRepository extends JpaRepository<PassengerFutureRequest, Long> {

    List<PassengerFutureRequest> findByPassenger(User passenger);

    List<PassengerFutureRequest> findByStatus(String status);
}
