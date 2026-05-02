package com.example.BorrowBoxBackend.dto.request;

import java.time.LocalDateTime;

public class BorrowRequest {
    private String itemId;
    private LocalDateTime dueDate;

    public BorrowRequest() {}

    public BorrowRequest(String itemId, LocalDateTime dueDate) {
        this.itemId = itemId;
        this.dueDate = dueDate;
    }

    public String getItemId() { return itemId; }
    public void setItemId(String itemId) { this.itemId = itemId; }
    
    public LocalDateTime getDueDate() { return dueDate; }
    public void setDueDate(LocalDateTime dueDate) { this.dueDate = dueDate; }
}
