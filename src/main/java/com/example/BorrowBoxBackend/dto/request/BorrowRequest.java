package com.example.BorrowBoxBackend.dto.request;

public class BorrowRequest {
    private String itemId;
    private Integer quantity;
    private Integer days;
    private String notes;

    public String getItemId() { return itemId; }
    public void setItemId(String itemId) { this.itemId = itemId; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public Integer getDays() { return days; }
    public void setDays(Integer days) { this.days = days; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}