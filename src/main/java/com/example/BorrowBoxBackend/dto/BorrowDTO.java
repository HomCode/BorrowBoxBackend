package com.example.BorrowBoxBackend.dto;

import java.time.LocalDateTime;

public class BorrowDTO {

    private String id;

    private String studentId;
    private String studentName;
    private String studentEmail;
    private String studentIdentifier;

    private String itemId;
    private String itemName;
    private String itemLocation;
    private Boolean hasItemImage;

    private String serialNumber;

    private LocalDateTime borrowDate;
    private LocalDateTime dueDate;
    private LocalDateTime returnDate;

    private String status;
    private String condition;
    private String notes;

    public BorrowDTO() {}

    public BorrowDTO(
            String id,
            String studentId,
            String itemId,
            String itemName,
            String serialNumber,
            LocalDateTime borrowDate,
            LocalDateTime dueDate,
            LocalDateTime returnDate,
            String status,
            String condition,
            String notes
    ) {
        this.id = id;
        this.studentId = studentId;
        this.itemId = itemId;
        this.itemName = itemName;
        this.serialNumber = serialNumber;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
        this.status = status;
        this.condition = condition;
        this.notes = notes;
    }

    public BorrowDTO(
            String id,
            String studentId,
            String studentName,
            String studentEmail,
            String studentIdentifier,
            String itemId,
            String itemName,
            String itemLocation,
            Boolean hasItemImage,
            String serialNumber,
            LocalDateTime borrowDate,
            LocalDateTime dueDate,
            LocalDateTime returnDate,
            String status,
            String condition,
            String notes
    ) {
        this.id = id;
        this.studentId = studentId;
        this.studentName = studentName;
        this.studentEmail = studentEmail;
        this.studentIdentifier = studentIdentifier;
        this.itemId = itemId;
        this.itemName = itemName;
        this.itemLocation = itemLocation;
        this.hasItemImage = hasItemImage;
        this.serialNumber = serialNumber;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
        this.status = status;
        this.condition = condition;
        this.notes = notes;
    }

    public String getId() {
        return id;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public String getStudentEmail() {
        return studentEmail;
    }

    public String getStudentIdentifier() {
        return studentIdentifier;
    }

    public String getItemId() {
        return itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public String getItemLocation() {
        return itemLocation;
    }

    public Boolean getHasItemImage() {
        return hasItemImage;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public LocalDateTime getBorrowDate() {
        return borrowDate;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public LocalDateTime getReturnDate() {
        return returnDate;
    }

    public String getStatus() {
        return status;
    }

    public String getCondition() {
        return condition;
    }

    public String getNotes() {
        return notes;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public void setStudentEmail(String studentEmail) {
        this.studentEmail = studentEmail;
    }

    public void setStudentIdentifier(String studentIdentifier) {
        this.studentIdentifier = studentIdentifier;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public void setItemLocation(String itemLocation) {
        this.itemLocation = itemLocation;
    }

    public void setHasItemImage(Boolean hasItemImage) {
        this.hasItemImage = hasItemImage;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public void setBorrowDate(LocalDateTime borrowDate) {
        this.borrowDate = borrowDate;
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }

    public void setReturnDate(LocalDateTime returnDate) {
        this.returnDate = returnDate;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}