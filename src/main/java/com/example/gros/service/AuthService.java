package com.example.gros.service;

import com.example.gros.dto.LoginRequest;
import com.example.gros.model.User;
import com.example.gros.model.LoginTracking;
import com.example.gros.repository.UserRepository;
import com.example.gros.repository.LoginTrackingRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final LoginTrackingRepository loginTrackingRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AuthService(UserRepository userRepository, LoginTrackingRepository loginTrackingRepository) {
        this.userRepository = userRepository;
        this.loginTrackingRepository = loginTrackingRepository;
    }

    @Transactional
    public User login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid email or password");
        }
        // Update login tracking
        LoginTracking tracking = new LoginTracking();
        tracking.setUser(user);
        tracking.setLastLogin(LocalDateTime.now());
        tracking.setIsNowLoggedIn("Y");
        loginTrackingRepository.save(tracking);
        return user;
    }
} 