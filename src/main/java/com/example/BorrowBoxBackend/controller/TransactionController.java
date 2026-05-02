package com.example.BorrowBoxBackend.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.BorrowBoxBackend.dto.TransactionDTO;
import com.example.BorrowBoxBackend.service.TransactionService;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    // Get All Transactions
    @GetMapping
    public ResponseEntity<?> getAllTransactions() {
        try {
            List<TransactionDTO> transactions = transactionService.getAllTransactions();
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("transactions", transactions);
            response.put("count", transactions.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return errorResponse("Failed to fetch transactions: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get Transaction by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getTransactionById(@PathVariable String id) {
        try {
            TransactionDTO transaction = transactionService.getTransactionById(id);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("transaction", transaction);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return errorResponse(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    // Get Student's Transactions
    @GetMapping("/student/{studentId}")
    public ResponseEntity<?> getStudentTransactions(@PathVariable String studentId) {
        try {
            List<TransactionDTO> transactions = transactionService.getStudentTransactions(studentId);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("transactions", transactions);
            response.put("count", transactions.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return errorResponse("Failed to fetch student transactions: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get Transactions by Status
    @GetMapping("/status/{status}")
    public ResponseEntity<?> getTransactionsByStatus(@PathVariable String status) {
        try {
            List<TransactionDTO> transactions = transactionService.getTransactionsByStatus(status);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("transactions", transactions);
            response.put("count", transactions.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return errorResponse("Failed to fetch transactions by status: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get Transactions by Item
    @GetMapping("/item/{itemId}")
    public ResponseEntity<?> getTransactionsByItem(@PathVariable String itemId) {
        try {
            List<TransactionDTO> transactions = transactionService.getTransactionsByItem(itemId);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("transactions", transactions);
            response.put("count", transactions.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return errorResponse("Failed to fetch transactions by item: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get Transactions by Date Range
    @GetMapping("/range")
    public ResponseEntity<?> getTransactionsByDateRange(
            @RequestParam String startDate,
            @RequestParam String endDate) {
        try {
            LocalDateTime start = LocalDateTime.parse(startDate);
            LocalDateTime end = LocalDateTime.parse(endDate);
            List<TransactionDTO> transactions = transactionService.getTransactionsByDateRange(start, end);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("transactions", transactions);
            response.put("count", transactions.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return errorResponse("Failed to fetch transactions by date range: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // Helper method for error responses
    private ResponseEntity<?> errorResponse(String message, HttpStatus status) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("message", message);
        return ResponseEntity.status(status).body(response);
    }
}
