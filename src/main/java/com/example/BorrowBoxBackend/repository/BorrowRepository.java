package com.example.BorrowBoxBackend.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.BorrowBoxBackend.model.Borrow;

@Repository
public interface BorrowRepository extends JpaRepository<Borrow, String> {
    List<Borrow> findByStudentId(String studentId);
    List<Borrow> findByItemId(String itemId);
    List<Borrow> findByStatus(String status);

    @Query("SELECT b FROM Borrow b WHERE b.studentId = :studentId AND b.status = :status")
    List<Borrow> findByStudentIdAndStatus(@Param("studentId") String studentId, @Param("status") String status);

    boolean existsByStudentIdAndItemIdAndStatusIn(
            String studentId,
            String itemId,
            List<String> statuses
    );

    @Query("SELECT b FROM Borrow b WHERE b.status = 'ACTIVE' AND b.dueDate < CURRENT_TIMESTAMP")
    List<Borrow> findOverdueItems();

    @Query("SELECT b FROM Borrow b WHERE b.borrowDate BETWEEN :startDate AND :endDate")
    List<Borrow> findByDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
}