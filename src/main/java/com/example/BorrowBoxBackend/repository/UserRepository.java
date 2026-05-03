package com.example.BorrowBoxBackend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.BorrowBoxBackend.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByEmail(String email);

    Optional<User> findByStudentId(String studentId);

    Optional<User> findByOrgId(String orgId);

    boolean existsByEmail(String email);

    @Query(value = "SELECT COUNT(*) FROM users WHERE LOWER(role) = 'student'", nativeQuery = true)
    long countStudents();

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.password = :password, u.updatedAt = CURRENT_TIMESTAMP WHERE u.id = :userId")
    void updatePassword(String userId, String password);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query(value = "UPDATE users SET profile_photo = ?2, photo_content_type = ?3, photo_updated_at = CURRENT_TIMESTAMP, updated_at = CURRENT_TIMESTAMP WHERE id = ?1", nativeQuery = true)
    void updatePhoto(String userId, byte[] photo, String contentType);
}