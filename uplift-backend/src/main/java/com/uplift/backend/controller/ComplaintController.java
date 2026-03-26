package com.uplift.backend.controller;

import com.uplift.backend.dto.common.ApiResponse;
import com.uplift.backend.dto.complaint.ComplaintDto;
import com.uplift.backend.entity.Complaint;
import com.uplift.backend.entity.User;
import com.uplift.backend.enums.ComplaintStatus;
import com.uplift.backend.service.ComplaintService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/complaints")
public class ComplaintController {

    private final ComplaintService complaintService;

    public ComplaintController(ComplaintService complaintService) {
        this.complaintService = complaintService;
    }

    // Any authenticated user submits complaint
    @PostMapping
    public ApiResponse<Complaint> submit(@AuthenticationPrincipal User user,
                                         @Valid @RequestBody ComplaintDto dto) {
        Complaint c = complaintService.submitComplaint(user, dto);
        return ApiResponse.ok("Complaint submitted", c);
    }

    // Admin: list all complaints
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<List<Complaint>> all() {
        return ApiResponse.ok("All complaints", complaintService.listAll());
    }

    // Admin: update complaint status
    @PostMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Complaint> updateStatus(@PathVariable Long id,
                                               @RequestParam ComplaintStatus status) {
        Complaint c = complaintService.updateStatus(id, status);
        return ApiResponse.ok("Status updated", c);
    }
}
