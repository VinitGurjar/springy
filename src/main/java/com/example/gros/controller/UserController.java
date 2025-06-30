package com.example.gros.controller;

import com.example.gros.dto.RegisterRequest;
import com.example.gros.dto.LoginRequest;
import com.example.gros.dto.PasswordChangeRequest;
import com.example.gros.dto.ApiResponse;
import com.example.gros.model.User;
import com.example.gros.service.UserService;
import com.example.gros.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@CrossOrigin
public class UserController {
    private final UserService userService;
    private final AuthService authService;

    public UserController(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        User user = userService.registerUser(request);
        user.setPassword(null); // Hide password
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        User user = authService.login(request);
        user.setPassword(null); // Hide password
        return ResponseEntity.ok(user);
    }

    @PutMapping("/{customerId}/password")
    public ResponseEntity<ApiResponse> changePassword(
            @PathVariable Integer customerId,
            @Valid @RequestBody PasswordChangeRequest request) {
        userService.changePassword(customerId, request);
        return ResponseEntity.ok(new ApiResponse(true, "Password changed successfully"));
    }
} 