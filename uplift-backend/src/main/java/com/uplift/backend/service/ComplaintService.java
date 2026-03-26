package com.uplift.backend.service;

import com.uplift.backend.dto.complaint.ComplaintDto;
import com.uplift.backend.entity.Complaint;
import com.uplift.backend.entity.User;
import com.uplift.backend.enums.ComplaintStatus;
import com.uplift.backend.enums.RideType;
import com.uplift.backend.repository.ComplaintRepository;
import com.uplift.backend.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
public class ComplaintService {

    private final ComplaintRepository complaintRepository;
    private final UserRepository userRepository;

    public ComplaintService(ComplaintRepository complaintRepository,
                            UserRepository userRepository) {
        this.complaintRepository = complaintRepository;
        this.userRepository = userRepository;
    }

    public Complaint submitComplaint(User fromUser, ComplaintDto dto) {
        Complaint c = new Complaint();
        c.setFromUser(fromUser);

        if (dto.getAgainstUserId() != null) {
            User against = userRepository.findById(dto.getAgainstUserId())
                    .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "User not found"));
            c.setAgainstUser(against);
        }

        if (dto.getRideType() != null) {
            c.setRideType(RideType.valueOf(dto.getRideType()));
        }

        c.setRideId(dto.getRideId());
        c.setMessage(dto.getMessage());
        c.setStatus(ComplaintStatus.OPEN);

        return complaintRepository.save(c);
    }

    public List<Complaint> listAll() {
        return complaintRepository.findAll();
    }

    public Complaint updateStatus(Long id, ComplaintStatus status) {
        Complaint c = complaintRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Complaint not found"));
        c.setStatus(status);
        if (status == ComplaintStatus.RESOLVED || status == ComplaintStatus.REJECTED) {
            c.setResolvedAt(java.time.LocalDateTime.now());
        }
        return complaintRepository.save(c);
    }
}
