package com.example.BorrowBoxBackend.repository;

import com.example.BorrowBoxBackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);

    // Update only password
    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.password = :password, u.updatedAt = CURRENT_TIMESTAMP WHERE u.id = :userId")
    void updatePassword(@Param("userId") String userId, @Param("password") String password);

    // Update only photo fields - FIXED VERSION
    @Modifying
    @Transactional
    @Query(value = "UPDATE users SET profile_photo = ?2, photo_content_type = ?3, photo_updated_at = CURRENT_TIMESTAMP, updated_at = CURRENT_TIMESTAMP WHERE id = ?1", nativeQuery = true)
    void updatePhoto(String userId, byte[] photo, String contentType);
}