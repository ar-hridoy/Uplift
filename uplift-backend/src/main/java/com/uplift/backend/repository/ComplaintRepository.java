package com.uplift.backend.repository;

import com.uplift.backend.entity.Complaint;
import com.uplift.backend.entity.User;
import com.uplift.backend.enums.ComplaintStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ComplaintRepository extends JpaRepository<Complaint, Long> {

    List<Complaint> findByFromUser(User fromUser);

    List<Complaint> findByAgainstUser(User againstUser);

    List<Complaint> findByStatus(ComplaintStatus status);
}
