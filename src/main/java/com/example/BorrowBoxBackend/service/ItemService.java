package com.example.BorrowBoxBackend.service;

import com.example.BorrowBoxBackend.dto.ItemDTO;
import com.example.BorrowBoxBackend.dto.request.ItemRequest;
import com.example.BorrowBoxBackend.model.Item;
import com.example.BorrowBoxBackend.repository.ItemRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemService {

    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    // Create Item
    public ItemDTO createItem(ItemRequest request, String createdBy) {
        Item item = new Item(request.getName(), request.getDescription(), request.getCategory(), 
                           request.getQuantity(), request.getSerialNumber());
        item.setCreatedBy(createdBy);
        Item savedItem = itemRepository.save(item);
        return convertToDTO(savedItem);
    }

    // Get All Items
    public List<ItemDTO> getAllItems() {
        return itemRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Get Item by ID
    public ItemDTO getItemById(String id) {
        return itemRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new RuntimeException("Item not found with id: " + id));
    }

    // Update Item
    public ItemDTO updateItem(String id, ItemRequest request) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found with id: " + id));

        item.setName(request.getName());
        item.setDescription(request.getDescription());
        item.setCategory(request.getCategory());
        item.setTotalQuantity(request.getQuantity());
        item.setAvailableQuantity(request.getQuantity());

        Item updatedItem = itemRepository.save(item);
        return convertToDTO(updatedItem);
    }

    // Delete Item
    public void deleteItem(String id) {
        if (!itemRepository.existsById(id)) {
            throw new RuntimeException("Item not found with id: " + id);
        }
        itemRepository.deleteById(id);
    }

    // Search Items
    public List<ItemDTO> searchItems(String searchTerm) {
        return itemRepository.searchItems(searchTerm).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Filter by Category
    public List<ItemDTO> getItemsByCategory(String category) {
        return itemRepository.findByCategory(category).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Get Available Items
    public List<ItemDTO> getAvailableItems() {
        return itemRepository.findByStatus("AVAILABLE").stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Reduce Available Quantity
    public void reduceAvailableQuantity(String itemId, Integer amount) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found with id: " + itemId));

        if (item.getAvailableQuantity() < amount) {
            throw new RuntimeException("Insufficient item quantity available");
        }

        item.setAvailableQuantity(item.getAvailableQuantity() - amount);
        itemRepository.save(item);
    }

    // Increase Available Quantity
    public void increaseAvailableQuantity(String itemId, Integer amount) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found with id: " + itemId));

        item.setAvailableQuantity(item.getAvailableQuantity() + amount);
        itemRepository.save(item);
    }

    // Helper method to convert Item to ItemDTO
    private ItemDTO convertToDTO(Item item) {
        return new ItemDTO(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getCategory(),
                item.getTotalQuantity(),
                item.getAvailableQuantity(),
                item.getSerialNumber(),
                item.getStatus(),
                item.getCreatedBy(),
                item.getCreatedAt(),
                item.getUpdatedAt()
        );
    }
}
