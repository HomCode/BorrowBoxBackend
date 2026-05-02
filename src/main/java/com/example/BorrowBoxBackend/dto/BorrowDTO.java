package com.example.BorrowBoxBackend.dto;

import java.time.LocalDateTime;

public class BorrowDTO {

    private String id;
    private String studentId;
    private String itemId;
    private String itemName; // ✅ ADD THIS
    private String serialNumber;

    private LocalDateTime borrowDate;
    private LocalDateTime dueDate;
    private LocalDateTime returnDate;

    private String status;
    private String condition;
    private String notes;

    public BorrowDTO() {}

    public BorrowDTO(String id, String studentId, String itemId, String itemName,
                     String serialNumber,
                     LocalDateTime borrowDate, LocalDateTime dueDate, LocalDateTime returnDate,
                     String status, String condition, String notes) {

        this.id = id;
        this.studentId = studentId;
        this.itemId = itemId;
        this.itemName = itemName; // ✅
        this.serialNumber = serialNumber;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
        this.status = status;
        this.condition = condition;
        this.notes = notes;
    }

    // getters/setters

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public String getItemId() { return itemId; }
    public void setItemId(String itemId) { this.itemId = itemId; }

    public String getItemName() { return itemName; } // ✅
    public void setItemName(String itemName) { this.itemName = itemName; }

    public String getSerialNumber() { return serialNumber; }
    public void setSerialNumber(String serialNumber) { this.serialNumber = serialNumber; }

    public LocalDateTime getBorrowDate() { return borrowDate; }
    public void setBorrowDate(LocalDateTime borrowDate) { this.borrowDate = borrowDate; }

    public LocalDateTime getDueDate() { return dueDate; }
    public void setDueDate(LocalDateTime dueDate) { this.dueDate = dueDate; }

    public LocalDateTime getReturnDate() { return returnDate; }
    public void setReturnDate(LocalDateTime returnDate) { this.returnDate = returnDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getCondition() { return condition; }
    public void setCondition(String condition) { this.condition = condition; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}