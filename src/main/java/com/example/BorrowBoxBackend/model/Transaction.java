package com.example.BorrowBoxBackend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "borrow_id", nullable = false)
    private String borrowId;

    @Column(name = "student_id", nullable = false)
    private String studentId;

    @Column(name = "item_id", nullable = false)
    private String itemId;

    @Column(name = "transaction_type", nullable = false)
    private String transactionType;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "borrow_date")
    private LocalDateTime borrowDate;

    @Column(name = "due_date")
    private LocalDateTime dueDate;

    @Column(name = "return_date")
    private LocalDateTime returnDate;

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
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public Transaction() {}

    public Transaction(String borrowId, String studentId, String itemId, String transactionType, String status) {
        this.borrowId = borrowId;
        this.studentId = studentId;
        this.itemId = itemId;
        this.transactionType = transactionType;
        this.status = status;
    }

    // Getters
    public String getId() { return id; }
    public String getBorrowId() { return borrowId; }
    public String getStudentId() { return studentId; }
    public String getItemId() { return itemId; }
    public String getTransactionType() { return transactionType; }
    public String getStatus() { return status; }
    public LocalDateTime getBorrowDate() { return borrowDate; }
    public LocalDateTime getDueDate() { return dueDate; }
    public LocalDateTime getReturnDate() { return returnDate; }
    public String getCondition() { return condition; }
    public String getNotes() { return notes; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    // Setters
    public void setBorrowId(String borrowId) { this.borrowId = borrowId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }
    public void setItemId(String itemId) { this.itemId = itemId; }
    public void setTransactionType(String transactionType) { this.transactionType = transactionType; }
    public void setStatus(String status) { this.status = status; }
    public void setBorrowDate(LocalDateTime borrowDate) { this.borrowDate = borrowDate; }
    public void setDueDate(LocalDateTime dueDate) { this.dueDate = dueDate; }
    public void setReturnDate(LocalDateTime returnDate) { this.returnDate = returnDate; }
    public void setCondition(String condition) { this.condition = condition; }
    public void setNotes(String notes) { this.notes = notes; }
}
