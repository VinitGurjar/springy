package com.example.gros.service;

import com.example.gros.dto.RegisterRequest;
import com.example.gros.dto.PasswordChangeRequest;
import com.example.gros.model.User;
import com.example.gros.model.LoginTracking;
import com.example.gros.repository.UserRepository;
import com.example.gros.repository.LoginTrackingRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final LoginTrackingRepository loginTrackingRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, LoginTrackingRepository loginTrackingRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.loginTrackingRepository = loginTrackingRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public User registerUser(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }
        User user = new User();
        user.setCustomerName(request.getCustomerName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setAddress(request.getAddress());
        user.setContactNumber(request.getContactNumber());
        user.setUserRole(request.getUserRole() != null ? request.getUserRole() : "CUSTOMER");
        User saved = userRepository.save(user);
        // Create login tracking record
        LoginTracking tracking = new LoginTracking();
        tracking.setUser(saved);
        loginTrackingRepository.save(tracking);
        return saved;
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> findById(Integer id) {
        return userRepository.findById(id);
    }

    @Transactional
    public void changePassword(Integer customerId, PasswordChangeRequest request) {
        User user = userRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Old password is incorrect");
        }
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new IllegalArgumentException("New password and confirm password do not match");
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
        // Update login tracking
        LoginTracking tracking = new LoginTracking();
        tracking.setUser(user);
        tracking.setUpdatedPassword(user.getPassword());
        tracking.setOldPassword(request.getOldPassword());
        tracking.setLastLogin(LocalDateTime.now());
        loginTrackingRepository.save(tracking);
    }
} 