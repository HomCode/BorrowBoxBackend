package com.example.BorrowBoxBackend.dto.request;

public class ReturnBorrowRequest {
    private String condition;
    private String notes;

    public ReturnBorrowRequest() {}

    public ReturnBorrowRequest(String condition, String notes) {
        this.condition = condition;
        this.notes = notes;
    }

    public String getCondition() { return condition; }
    public void setCondition(String condition) { this.condition = condition; }
    
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
