package com.example.gros.controller;

import com.example.gros.dto.ApiResponse;
import com.example.gros.dto.LoginRequest;
import com.example.gros.dto.RegisterRequest;
import com.example.gros.dto.UserDTO;
import com.example.gros.model.User;
import com.example.gros.service.AuthService;
import com.example.gros.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
public class AuthController {
    private final AuthService authService;
    private final UserService userService;

    public AuthController(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        User user = userService.registerUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new UserDTO(user));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request, HttpServletRequest servletRequest) {
        User user = authService.login(request);
        
        // Create session
        HttpSession session = servletRequest.getSession(true);
        session.setAttribute("userId", user.getCustomerId());
        session.setAttribute("userRole", user.getUserRole());
        
        return ResponseEntity.ok(new UserDTO(user));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse> logout(HttpServletRequest servletRequest) {
        HttpSession session = servletRequest.getSession(false);
        if (session != null) {
            Integer userId = (Integer) session.getAttribute("userId");
            if (userId != null) {
                authService.logout(userId);
            }
            session.invalidate();
        }
        return ResponseEntity.ok(new ApiResponse(true, "Logged out successfully"));
    }
    
    @PostMapping("/admin/create")
    public ResponseEntity<?> createAdmin(@Valid @RequestBody RegisterRequest request, HttpServletRequest servletRequest) {
        // Verify admin access
        HttpSession session = servletRequest.getSession(false);
        if (session == null || !"ADMIN".equals(session.getAttribute("userRole"))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse(false, "Only admins can create other admin accounts"));
        }
        
        // Force role to be ADMIN
        request.setUserRole("ADMIN");
        User admin = userService.registerUser(request);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(new UserDTO(admin));
    }
} 