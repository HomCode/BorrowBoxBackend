package com.example.BorrowBoxBackend.dto.request;

public class ItemRequest {
    private String name;
    private String description;
    private String category;
    private Integer quantity;
    private String serialNumber;

    public ItemRequest() {}

    public ItemRequest(String name, String description, String category, Integer quantity, String serialNumber) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.quantity = quantity;
        this.serialNumber = serialNumber;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    
    public String getSerialNumber() { return serialNumber; }
    public void setSerialNumber(String serialNumber) { this.serialNumber = serialNumber; }
}
