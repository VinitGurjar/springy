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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
public class AuthController {
    private final AuthService authService;
    private final UserService userService;

    public AuthController(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        try {
            User user = userService.registerUser(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse(true, "User registered successfully", new UserDTO(user)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request, HttpServletRequest servletRequest) {
        try {
            User user = authService.login(request);
            
            // Create session
            HttpSession session = servletRequest.getSession(true);
            session.setAttribute("userId", user.getCustomerId());
            session.setAttribute("userRole", user.getUserRole());
            session.setMaxInactiveInterval(3600); // 1 hour session timeout
            
            return ResponseEntity.ok(new ApiResponse(true, "Login successful", new UserDTO(user)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse(false, e.getMessage()));
        }
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
        
        // Clear security context
        SecurityContextHolder.clearContext();
        
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
        
        try {
            // Force role to be ADMIN
            request.setUserRole("ADMIN");
            User admin = userService.registerUser(request);
            
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse(true, "Admin user created successfully", new UserDTO(admin)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, e.getMessage()));
        }
    }

    @GetMapping("/session-info")
    public ResponseEntity<?> getSessionInfo(HttpServletRequest servletRequest) {
        HttpSession session = servletRequest.getSession(false);
        if (session != null && session.getAttribute("userId") != null) {
            Integer userId = (Integer) session.getAttribute("userId");
            String userRole = (String) session.getAttribute("userRole");
            return ResponseEntity.ok(new ApiResponse(true, "Session active", 
                    java.util.Map.of("userId", userId, "userRole", userRole)));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ApiResponse(false, "No active session"));
    }
} 