package com.uplift.backend.repository;

import com.uplift.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    // 🔥 Add this
    Optional<User> findByUsernameOrEmail(String username, String email);

    // 🔥 Needed for registration
    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}
