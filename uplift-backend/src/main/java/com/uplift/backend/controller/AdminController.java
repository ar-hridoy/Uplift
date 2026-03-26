package com.uplift.backend.controller;

import com.uplift.backend.dto.admin.CreateRiderDto;
import com.uplift.backend.entity.User;
import com.uplift.backend.enums.UserRole;
import com.uplift.backend.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminController(UserRepository userRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/create-rider")
    public ResponseEntity<User> createRider(@Valid @RequestBody CreateRiderDto dto) {
        User r = new User();
        r.setFullName(dto.getFullName());
        r.setUsername(dto.getUsername());
        r.setEmail(dto.getEmail());
        r.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
        r.setRole(UserRole.RIDER);
        r.setActive(true);

        return ResponseEntity.ok(userRepository.save(r));
    }

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @PostMapping("/users/{id}/activate")
    public ResponseEntity<Void> activate(@PathVariable Long id,
                                         @RequestParam boolean active) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        user.setActive(active);
        userRepository.save(user);
        return ResponseEntity.ok().build();
    }
}
