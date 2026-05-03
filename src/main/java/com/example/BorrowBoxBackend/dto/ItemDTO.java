package com.example.BorrowBoxBackend.dto;

import java.time.LocalDateTime;

public class ItemDTO {
    private String id;
    private String name;
    private String description;
    private String category;
    private String location;
    private Integer totalQuantity;
    private Integer availableQuantity;
    private String serialNumber;
    private String status;
    private String createdBy;

    private boolean hasImage;
    private String imageContentType;
    private LocalDateTime imageUpdatedAt;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ItemDTO() {}

    public ItemDTO(
            String id,
            String name,
            String description,
            String category,
            String location,
            Integer totalQuantity,
            Integer availableQuantity,
            String serialNumber,
            String status,
            String createdBy,
            boolean hasImage,
            String imageContentType,
            LocalDateTime imageUpdatedAt,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.category = category;
        this.location = location;
        this.totalQuantity = totalQuantity;
        this.availableQuantity = availableQuantity;
        this.serialNumber = serialNumber;
        this.status = status;
        this.createdBy = createdBy;
        this.hasImage = hasImage;
        this.imageContentType = imageContentType;
        this.imageUpdatedAt = imageUpdatedAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
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
    
    public Integer getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(Integer totalQuantity) {
        this.totalQuantity = totalQuantity;
    }
    
    public Integer getAvailableQuantity() {
        return availableQuantity;
    }

    public void setAvailableQuantity(Integer availableQuantity) {
        this.availableQuantity = availableQuantity;
    }
    
    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }
    
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public boolean isHasImage() {
        return hasImage;
    }

    public void setHasImage(boolean hasImage) {
        this.hasImage = hasImage;
    }

    public String getImageContentType() {
        return imageContentType;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }

    public LocalDateTime getImageUpdatedAt() {
        return imageUpdatedAt;
    }

    public void setImageUpdatedAt(LocalDateTime imageUpdatedAt) {
        this.imageUpdatedAt = imageUpdatedAt;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}