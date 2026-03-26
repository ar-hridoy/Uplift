package com.uplift.backend.service;

import com.uplift.backend.dto.admin.AdminCreateRiderDto;
import com.uplift.backend.dto.admin.UserCreatedResponse;
import com.uplift.backend.entity.User;
import com.uplift.backend.enums.UserRole;
import com.uplift.backend.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AdminRiderService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminRiderService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserCreatedResponse createRider(AdminCreateRiderDto dto) {

        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "EMAIL_ALREADY_EXISTS");
        }
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "USERNAME_ALREADY_EXISTS");
        }

        User u = new User();
        u.setFullName(dto.getFullName());
        u.setUsername(dto.getUsername());
        u.setEmail(dto.getEmail());

        // ✅ role is enum
        u.setRole(UserRole.RIDER);

        // ✅ active is boolean
        u.setActive(dto.isActive());

        u.setPasswordHash(passwordEncoder.encode(dto.getPassword()));

        User saved = userRepository.save(u);

        return new UserCreatedResponse(
                saved.getId(),
                saved.getFullName(),
                saved.getUsername(),
                saved.getEmail(),

                // ✅ response wants String, so convert enum to String
                saved.getRole().name(),

                saved.isActive() ? "ACTIVE" : "INACTIVE"

        );
    }
}
