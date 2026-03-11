package com.example.BorrowBoxBackend.controller;  // This file is in controller package

import com.example.BorrowBoxBackend.dto.LoginRequest;  // ✅ Fixed import
import com.example.BorrowBoxBackend.dto.RegisterRequest;  // ✅ Fixed import
import com.example.BorrowBoxBackend.dto.AuthResponse;  // ✅ Fixed import
import com.example.BorrowBoxBackend.service.AuthService;  // ✅ Fixed import
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}