package com.example.BorrowBoxBackend.repository;

import com.example.BorrowBoxBackend.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

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
}
