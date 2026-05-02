package com.example.BorrowBoxBackend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "borrows")
public class Borrow {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "student_id", nullable = false)
    private String studentId;

    @Column(name = "item_id", nullable = false)
    private String itemId;

    @Column(name = "serial_number", nullable = false)
    private String serialNumber;

    @Column(name = "borrow_date", nullable = false)
    private LocalDateTime borrowDate;

    @Column(name = "due_date", nullable = false)
    private LocalDateTime dueDate;

    @Column(name = "return_date")
    private LocalDateTime returnDate;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "condition")
    private String condition;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        status = "ACTIVE";
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public Borrow() {}

    public Borrow(String studentId, String itemId, String serialNumber, LocalDateTime borrowDate, LocalDateTime dueDate) {
        this.studentId = studentId;
        this.itemId = itemId;
        this.serialNumber = serialNumber;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
        this.status = "ACTIVE";
    }

    // Getters
    public String getId() { return id; }
    public String getStudentId() { return studentId; }
    public String getItemId() { return itemId; }
    public String getSerialNumber() { return serialNumber; }
    public LocalDateTime getBorrowDate() { return borrowDate; }
    public LocalDateTime getDueDate() { return dueDate; }
    public LocalDateTime getReturnDate() { return returnDate; }
    public String getStatus() { return status; }
    public String getCondition() { return condition; }
    public String getNotes() { return notes; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    // Setters
    public void setStudentId(String studentId) { this.studentId = studentId; }
    public void setItemId(String itemId) { this.itemId = itemId; }
    public void setSerialNumber(String serialNumber) { this.serialNumber = serialNumber; }
    public void setBorrowDate(LocalDateTime borrowDate) { this.borrowDate = borrowDate; }
    public void setDueDate(LocalDateTime dueDate) { this.dueDate = dueDate; }
    public void setReturnDate(LocalDateTime returnDate) { this.returnDate = returnDate; }
    public void setStatus(String status) { this.status = status; }
    public void setCondition(String condition) { this.condition = condition; }
    public void setNotes(String notes) { this.notes = notes; }
}
