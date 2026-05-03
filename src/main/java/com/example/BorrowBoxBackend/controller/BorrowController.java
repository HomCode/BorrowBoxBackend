package com.example.BorrowBoxBackend.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.BorrowBoxBackend.dto.BorrowDTO;
import com.example.BorrowBoxBackend.dto.request.BorrowRequest;
import com.example.BorrowBoxBackend.dto.request.ReturnBorrowRequest;
import com.example.BorrowBoxBackend.model.User;
import com.example.BorrowBoxBackend.repository.UserRepository;
import com.example.BorrowBoxBackend.security.JwtUtils;
import com.example.BorrowBoxBackend.service.BorrowService;

@RestController
@RequestMapping("/api/borrows")
public class BorrowController {

    private final BorrowService borrowService;
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;

    public BorrowController(
            BorrowService borrowService,
            JwtUtils jwtUtils,
            UserRepository userRepository
    ) {
        this.borrowService = borrowService;
        this.jwtUtils = jwtUtils;
        this.userRepository = userRepository;
    }

    @PostMapping("/borrow")
    public ResponseEntity<?> borrowItem(
            @RequestBody BorrowRequest request,
            @RequestHeader(value = "Authorization", required = false) String auth
    ) {
        try {
            String studentId = extractUserIdFromToken(auth);

            BorrowDTO borrow = borrowService.borrowItem(studentId, request);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Item borrowed successfully");
            response.put("borrow", borrow);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return errorResponse("Failed to borrow item: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/{borrowId}/return")
    public ResponseEntity<?> returnItem(
            @PathVariable String borrowId,
            @RequestBody ReturnBorrowRequest request
    ) {
        try {
            BorrowDTO borrow = borrowService.returnItem(borrowId, request);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Item returned successfully");
            response.put("borrow", borrow);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return errorResponse("Failed to return item: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/student")
    public ResponseEntity<?> getStudentBorrows(
            @RequestHeader(value = "Authorization", required = false) String auth
    ) {
        try {
            String studentId = extractUserIdFromToken(auth);

            List<BorrowDTO> borrows = borrowService.getStudentBorrows(studentId);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("borrows", borrows);
            response.put("count", borrows.size());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return errorResponse("Failed to fetch borrows: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/student/active")
    public ResponseEntity<?> getStudentActiveBorrows(
            @RequestHeader(value = "Authorization", required = false) String auth
    ) {
        try {
            String studentId = extractUserIdFromToken(auth);

            List<BorrowDTO> borrows = borrowService.getStudentActiveBorrows(studentId);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("borrows", borrows);
            response.put("count", borrows.size());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return errorResponse("Failed to fetch active borrows: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllBorrows() {
        try {
            List<BorrowDTO> borrows = borrowService.getAllBorrows();

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("borrows", borrows);
            response.put("count", borrows.size());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return errorResponse("Failed to fetch borrows: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBorrowById(@PathVariable String id) {
        try {
            BorrowDTO borrow = borrowService.getBorrowById(id);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("borrow", borrow);

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return errorResponse(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/overdue/all")
    public ResponseEntity<?> getOverdueItems() {
        try {
            List<BorrowDTO> borrows = borrowService.getOverdueItems();

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("borrows", borrows);
            response.put("count", borrows.size());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return errorResponse("Failed to fetch overdue items: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private String extractUserIdFromToken(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Authorization token is required");
        }

        String token = authHeader.substring(7);
        String email = jwtUtils.getUsernameFromToken(token);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Authenticated user not found"));

        return user.getId();
    }

    private ResponseEntity<?> errorResponse(String message, HttpStatus status) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("message", message);

        return ResponseEntity.status(status).body(response);
    }
}