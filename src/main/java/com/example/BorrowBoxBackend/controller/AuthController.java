package com.example.BorrowBoxBackend.controller;  // This file is in controller package

import org.springframework.http.ResponseEntity;  // ✅ Fixed import
import org.springframework.web.bind.annotation.PostMapping;  // ✅ Fixed import
import org.springframework.web.bind.annotation.RequestBody;  // ✅ Fixed import
import org.springframework.web.bind.annotation.RequestMapping;  // ✅ Fixed import
import org.springframework.web.bind.annotation.RestController;

import com.example.BorrowBoxBackend.dto.AuthResponse;
import com.example.BorrowBoxBackend.dto.LoginRequest;
import com.example.BorrowBoxBackend.dto.RegisterRequest;
import com.example.BorrowBoxBackend.service.AuthService;

@RestController
@RequestMapping("/api/auth")
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