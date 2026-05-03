package com.example.BorrowBoxBackend.dto.request;

public class ItemRequest {

    private String name;
    private String description;
    private String category;
    private String location;
    private Integer quantity;
    private String serialNumber;

    public ItemRequest() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

	public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

	public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

	public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

	public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

	public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }
}