package com.example.BorrowBoxBackend.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.BorrowBoxBackend.dto.ItemDTO;
import com.example.BorrowBoxBackend.dto.request.ItemRequest;
import com.example.BorrowBoxBackend.service.ItemService;

@RestController
@RequestMapping("/api/items")
public class ItemController {

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    // Create Item
    @PostMapping("/create")
    public ResponseEntity<?> createItem(@RequestBody ItemRequest request, @RequestHeader(value = "Authorization", required = false) String auth) {
        try {
            String userId = extractUserIdFromToken(auth);
            ItemDTO item = itemService.createItem(request, userId);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Item created successfully");
            response.put("item", item);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return errorResponse("Failed to create item: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // Get All Items
    @GetMapping
    public ResponseEntity<?> getAllItems() {
        try {
            List<ItemDTO> items = itemService.getAllItems();
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("items", items);
            response.put("count", items.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return errorResponse("Failed to fetch items: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get Item by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getItemById(@PathVariable String id) {
        try {
            ItemDTO item = itemService.getItemById(id);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("item", item);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return errorResponse(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    // Update Item
    @PutMapping("/{id}")
    public ResponseEntity<?> updateItem(@PathVariable String id, @RequestBody ItemRequest request) {
        try {
            ItemDTO item = itemService.updateItem(id, request);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Item updated successfully");
            response.put("item", item);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return errorResponse(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    // Delete Item
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteItem(@PathVariable String id) {
        try {
            itemService.deleteItem(id);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Item deleted successfully");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return errorResponse(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    // Search Items
    @GetMapping("/search/{searchTerm}")
    public ResponseEntity<?> searchItems(@PathVariable String searchTerm) {
        try {
            List<ItemDTO> items = itemService.searchItems(searchTerm);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("items", items);
            response.put("count", items.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return errorResponse("Search failed: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Filter by Category
    @GetMapping("/category/{category}")
    public ResponseEntity<?> getItemsByCategory(@PathVariable String category) {
        try {
            List<ItemDTO> items = itemService.getItemsByCategory(category);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("items", items);
            response.put("count", items.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return errorResponse("Failed to fetch items by category: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get Available Items
    @GetMapping("/available/all")
    public ResponseEntity<?> getAvailableItems() {
        try {
            List<ItemDTO> items = itemService.getAvailableItems();
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("items", items);
            response.put("count", items.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return errorResponse("Failed to fetch available items: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Helper method to extract user ID from token (simplified)
    private String extractUserIdFromToken(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return "system-user"; // In production, decode JWT token
        }
        return "system-user";
    }

    // Helper method for error responses
    private ResponseEntity<?> errorResponse(String message, HttpStatus status) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("message", message);
        return ResponseEntity.status(status).body(response);
    }
}
