package com.example.BorrowBoxBackend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.BorrowBoxBackend.model.Item;

@Repository
public interface ItemRepository extends JpaRepository<Item, String> {

    Optional<Item> findBySerialNumber(String serialNumber);

    List<Item> findByCategory(String category);

    List<Item> findByNameContainingIgnoreCase(String name);

    List<Item> findByStatus(String status);
    
    @Query("SELECT i FROM Item i WHERE LOWER(i.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR LOWER(i.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Item> searchItems(@Param("searchTerm") String searchTerm);
    
    @Query("SELECT i FROM Item i WHERE i.category = :category AND LOWER(i.name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Item> searchItemsByCategory(@Param("category") String category, @Param("searchTerm") String searchTerm);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query(value = "UPDATE items SET item_image = ?2, image_content_type = ?3, image_updated_at = CURRENT_TIMESTAMP, updated_at = CURRENT_TIMESTAMP WHERE id = ?1", nativeQuery = true)
    void updateItemPhoto(String itemId, byte[] image, String contentType);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query(value = "UPDATE items SET item_image = NULL, image_content_type = NULL, image_updated_at = NULL, updated_at = CURRENT_TIMESTAMP WHERE id = ?1", nativeQuery = true)
    void deleteItemPhoto(String itemId);
}