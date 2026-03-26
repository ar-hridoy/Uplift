package com.uplift.backend.service;

import com.uplift.backend.entity.User;
import com.uplift.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(Long id) {
        return  userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public void setActive(Long id, boolean active) {
        User u = findById(id);
        u.setActive(active);
        userRepository.save(u);
    }
}
