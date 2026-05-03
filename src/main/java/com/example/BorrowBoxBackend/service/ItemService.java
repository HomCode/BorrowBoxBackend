package com.example.BorrowBoxBackend.service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.BorrowBoxBackend.dto.ItemDTO;
import com.example.BorrowBoxBackend.dto.request.ItemRequest;
import com.example.BorrowBoxBackend.model.Item;
import com.example.BorrowBoxBackend.repository.ItemRepository;

@Service
public class ItemService {

    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public ItemDTO createItem(ItemRequest request, String createdBy) {
        Item item = new Item(
                request.getName(),
                request.getDescription(),
                request.getCategory(),
                request.getQuantity(),
                request.getSerialNumber()
        );

        item.setLocation(request.getLocation());
        item.setCreatedBy(createdBy);
        item.setStatus("AVAILABLE");

        Item savedItem = itemRepository.save(item);
        return convertToDTO(savedItem);
    }

    public List<ItemDTO> getAllItems() {
        return itemRepository.findAll().stream()
                .filter(item -> !isDeleted(item))
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public ItemDTO getItemById(String id) {
        return itemRepository.findById(id)
                .filter(item -> !isDeleted(item))
                .map(this::convertToDTO)
                .orElseThrow(() -> new RuntimeException("Item not found with id: " + id));
    }

    public Item getItemEntityById(String id) {
        return itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found with id: " + id));
    }

    public ItemDTO updateItem(String id, ItemRequest request) {
        Item item = itemRepository.findById(id)
                .filter(existingItem -> !isDeleted(existingItem))
                .orElseThrow(() -> new RuntimeException("Item not found with id: " + id));

        item.setName(request.getName());
        item.setDescription(request.getDescription());
        item.setCategory(request.getCategory());
        item.setLocation(request.getLocation());
        item.setSerialNumber(request.getSerialNumber());

        Integer newTotalQuantity = request.getQuantity();

        if (newTotalQuantity != null) {
            Integer oldTotalQuantity = item.getTotalQuantity() != null ? item.getTotalQuantity() : 0;
            Integer oldAvailableQuantity = item.getAvailableQuantity() != null ? item.getAvailableQuantity() : 0;

            int borrowedQuantity = Math.max(oldTotalQuantity - oldAvailableQuantity, 0);

            item.setTotalQuantity(newTotalQuantity);

            int newAvailableQuantity = Math.max(newTotalQuantity - borrowedQuantity, 0);
            item.setAvailableQuantity(newAvailableQuantity);

            if (newAvailableQuantity > 0) {
                item.setStatus("AVAILABLE");
            } else {
                item.setStatus("UNAVAILABLE");
            }
        }

        Item updatedItem = itemRepository.save(item);
        return convertToDTO(updatedItem);
    }

    // SOFT DELETE ONLY
    // This removes the item from officer/student item lists,
    // but keeps old transaction records accurate.
    public void deleteItem(String id) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found with id: " + id));

        item.setStatus("DELETED");
        itemRepository.save(item);
    }

    public ItemDTO uploadItemPhoto(String itemId, MultipartFile file) throws IOException {
        Item item = itemRepository.findById(itemId)
                .filter(existingItem -> !isDeleted(existingItem))
                .orElseThrow(() -> new RuntimeException("Item not found with id: " + itemId));

        String contentType = file.getContentType();

        if (contentType == null ||
                (!contentType.equals("image/jpeg") &&
                 !contentType.equals("image/jpg") &&
                 !contentType.equals("image/png"))) {
            throw new RuntimeException("Only JPG and PNG images are allowed");
        }

        if (file.getSize() > 5 * 1024 * 1024) {
            throw new RuntimeException("File size must be less than 5MB");
        }

        itemRepository.updateItemPhoto(item.getId(), file.getBytes(), contentType);

        Item updatedItem = itemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found with id: " + itemId));

        return convertToDTO(updatedItem);
    }

    public ItemDTO deleteItemPhoto(String itemId) {
        Item item = itemRepository.findById(itemId)
                .filter(existingItem -> !isDeleted(existingItem))
                .orElseThrow(() -> new RuntimeException("Item not found with id: " + itemId));

        itemRepository.deleteItemPhoto(item.getId());

        Item updatedItem = itemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found with id: " + itemId));

        return convertToDTO(updatedItem);
    }

    public List<ItemDTO> searchItems(String searchTerm) {
        return itemRepository.searchItems(searchTerm).stream()
                .filter(item -> !isDeleted(item))
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<ItemDTO> getItemsByCategory(String category) {
        return itemRepository.findByCategory(category).stream()
                .filter(item -> !isDeleted(item))
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<ItemDTO> getAvailableItems() {
        return itemRepository.findByStatus("AVAILABLE").stream()
                .filter(item -> !isDeleted(item))
                .filter(item -> item.getAvailableQuantity() != null && item.getAvailableQuantity() > 0)
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public void reduceAvailableQuantity(String itemId, Integer amount) {
        Item item = itemRepository.findById(itemId)
                .filter(existingItem -> !isDeleted(existingItem))
                .orElseThrow(() -> new RuntimeException("Item not found with id: " + itemId));

        if (item.getAvailableQuantity() == null || item.getAvailableQuantity() < amount) {
            throw new RuntimeException("Insufficient item quantity available");
        }

        int newAvailableQuantity = item.getAvailableQuantity() - amount;
        item.setAvailableQuantity(Math.max(newAvailableQuantity, 0));

        if (item.getAvailableQuantity() > 0) {
            item.setStatus("AVAILABLE");
        } else {
            item.setStatus("UNAVAILABLE");
        }

        itemRepository.save(item);
    }

    public void increaseAvailableQuantity(String itemId, Integer amount) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found with id: " + itemId));

        int currentAvailable = item.getAvailableQuantity() != null ? item.getAvailableQuantity() : 0;
        int totalQuantity = item.getTotalQuantity() != null ? item.getTotalQuantity() : 0;

        int newAvailableQuantity = currentAvailable + amount;

        if (newAvailableQuantity > totalQuantity) {
            newAvailableQuantity = totalQuantity;
        }

        item.setAvailableQuantity(newAvailableQuantity);

        // Important:
        // If item was deleted/archived, keep it deleted even after return.
        if (!isDeleted(item)) {
            if (item.getAvailableQuantity() > 0) {
                item.setStatus("AVAILABLE");
            } else {
                item.setStatus("UNAVAILABLE");
            }
        }

        itemRepository.save(item);
    }

    private boolean isDeleted(Item item) {
        return item.getStatus() != null && item.getStatus().equalsIgnoreCase("DELETED");
    }

    private ItemDTO convertToDTO(Item item) {
        return new ItemDTO(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getCategory(),
                item.getLocation(),
                item.getTotalQuantity(),
                item.getAvailableQuantity(),
                item.getSerialNumber(),
                item.getStatus(),
                item.getCreatedBy(),
                item.getItemImage() != null,
                item.getImageContentType(),
                item.getImageUpdatedAt(),
                item.getCreatedAt(),
                item.getUpdatedAt()
        );
    }
}