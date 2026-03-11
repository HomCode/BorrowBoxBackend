package com.example.BorrowBoxBackend.repository;

import com.example.BorrowBoxBackend.model.User;  // ✅ Fixed import
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
}