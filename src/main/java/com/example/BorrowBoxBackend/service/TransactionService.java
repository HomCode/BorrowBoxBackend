package com.example.BorrowBoxBackend.service;

import com.example.BorrowBoxBackend.dto.TransactionDTO;
import com.example.BorrowBoxBackend.model.Transaction;
import com.example.BorrowBoxBackend.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    // Create Transaction
    public TransactionDTO createTransaction(String borrowId, String studentId, String itemId, 
                                           String transactionType, String status) {
        Transaction transaction = new Transaction(borrowId, studentId, itemId, transactionType, status);
        transaction.setBorrowDate(LocalDateTime.now());
        Transaction savedTransaction = transactionRepository.save(transaction);
        return convertToDTO(savedTransaction);
    }

    // Update Transaction
    public TransactionDTO updateTransaction(String borrowId, String transactionType, String status, 
                                           String condition, String notes) {
        Transaction transaction = transactionRepository.findByBorrowId(borrowId).stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Transaction not found for borrow: " + borrowId));

        transaction.setTransactionType(transactionType);
        transaction.setStatus(status);
        transaction.setCondition(condition);
        transaction.setNotes(notes);
        transaction.setReturnDate(LocalDateTime.now());
        
        Transaction updatedTransaction = transactionRepository.save(transaction);
        return convertToDTO(updatedTransaction);
    }

    // Get All Transactions
    public List<TransactionDTO> getAllTransactions() {
        return transactionRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Get Transaction by ID
    public TransactionDTO getTransactionById(String id) {
        return transactionRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new RuntimeException("Transaction not found with id: " + id));
    }

    // Get Student's Transactions
    public List<TransactionDTO> getStudentTransactions(String studentId) {
        return transactionRepository.findByStudentIdOrderByDate(studentId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Get Transactions by Status
    public List<TransactionDTO> getTransactionsByStatus(String status) {
        return transactionRepository.findByStatusOrderByDate(status).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Get Transactions by Item
    public List<TransactionDTO> getTransactionsByItem(String itemId) {
        return transactionRepository.findByItemId(itemId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Get Transactions by Date Range
    public List<TransactionDTO> getTransactionsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return transactionRepository.findByDateRange(startDate, endDate).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Helper method to convert Transaction to TransactionDTO
    private TransactionDTO convertToDTO(Transaction transaction) {
        return new TransactionDTO(
                transaction.getId(),
                transaction.getBorrowId(),
                transaction.getStudentId(),
                transaction.getItemId(),
                transaction.getTransactionType(),
                transaction.getStatus(),
                transaction.getBorrowDate(),
                transaction.getDueDate(),
                transaction.getReturnDate(),
                transaction.getCondition(),
                transaction.getNotes()
        );
    }
}
