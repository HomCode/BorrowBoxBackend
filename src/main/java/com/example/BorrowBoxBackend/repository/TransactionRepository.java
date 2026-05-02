package com.example.BorrowBoxBackend.repository;

import com.example.BorrowBoxBackend.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, String> {
    List<Transaction> findByBorrowId(String borrowId);
    List<Transaction> findByStudentId(String studentId);
    List<Transaction> findByItemId(String itemId);
    List<Transaction> findByStatus(String status);
    
    @Query("SELECT t FROM Transaction t WHERE t.transactionType = :transactionType")
    List<Transaction> findByTransactionType(@Param("transactionType") String transactionType);
    
    @Query("SELECT t FROM Transaction t WHERE t.borrowDate BETWEEN :startDate AND :endDate ORDER BY t.borrowDate DESC")
    List<Transaction> findByDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT t FROM Transaction t WHERE t.studentId = :studentId ORDER BY t.borrowDate DESC")
    List<Transaction> findByStudentIdOrderByDate(@Param("studentId") String studentId);
    
    @Query("SELECT t FROM Transaction t WHERE t.status = :status ORDER BY t.borrowDate DESC")
    List<Transaction> findByStatusOrderByDate(@Param("status") String status);
}
