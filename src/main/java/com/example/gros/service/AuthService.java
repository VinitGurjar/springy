package com.example.gros.service;

import com.example.gros.dto.LoginRequest;
import com.example.gros.model.User;
import com.example.gros.model.LoginTracking;
import com.example.gros.repository.UserRepository;
import com.example.gros.repository.LoginTrackingRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final LoginTrackingRepository loginTrackingRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, LoginTrackingRepository loginTrackingRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.loginTrackingRepository = loginTrackingRepository;
        this.passwordEncoder = passwordEncoder;
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
    
    @Transactional
    public void logout(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        
        // Update login tracking
        LoginTracking tracking = new LoginTracking();
        tracking.setUser(user);
        tracking.setLastLogout(LocalDateTime.now());
        tracking.setIsNowLoggedIn("N");
        loginTrackingRepository.save(tracking);
    }
} 