package com.uplift.backend.controller.admin;

import com.uplift.backend.dto.admin.AdminCreateRiderDto;
import com.uplift.backend.dto.admin.UserCreatedResponse;
import com.uplift.backend.service.AdminRiderService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminRiderController {

    private final AdminRiderService adminRiderService;

    public AdminRiderController(AdminRiderService adminRiderService) {
        this.adminRiderService = adminRiderService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/riders")
    public ResponseEntity<UserCreatedResponse> createRider(@Valid @RequestBody AdminCreateRiderDto dto) {
        return ResponseEntity.ok(adminRiderService.createRider(dto));
    }

}
