package com.uplift.backend.repository;

import com.uplift.backend.entity.RiderOffer;
import com.uplift.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RiderOfferRepository extends JpaRepository<RiderOffer, Long> {

    List<RiderOffer> findByRider(User rider);

    List<RiderOffer> findByFutureRequest_Id(Long futureRequestId);
}
