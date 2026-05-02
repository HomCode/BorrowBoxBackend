package com.example.BorrowBoxBackend.service;

import com.example.BorrowBoxBackend.dto.BorrowDTO;
import com.example.BorrowBoxBackend.dto.request.BorrowRequest;
import com.example.BorrowBoxBackend.dto.request.ReturnBorrowRequest;
import com.example.BorrowBoxBackend.model.Borrow;
import com.example.BorrowBoxBackend.model.Item;
import com.example.BorrowBoxBackend.repository.BorrowRepository;
import com.example.BorrowBoxBackend.repository.ItemRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BorrowService {

    private final BorrowRepository borrowRepository;
    private final ItemRepository itemRepository;
    private final TransactionService transactionService;

    public BorrowService(BorrowRepository borrowRepository, ItemRepository itemRepository, TransactionService transactionService) {
        this.borrowRepository = borrowRepository;
        this.itemRepository = itemRepository;
        this.transactionService = transactionService;
    }

    // Borrow Item
    public BorrowDTO borrowItem(String studentId, BorrowRequest request) {
        Item item = itemRepository.findById(request.getItemId())
                .orElseThrow(() -> new RuntimeException("Item not found"));

        if (item.getAvailableQuantity() <= 0) {
            throw new RuntimeException("Item is not available for borrowing");
        }

        // Create Borrow record
        Borrow borrow = new Borrow(studentId, item.getId(), item.getSerialNumber(), 
                                  LocalDateTime.now(), request.getDueDate());
        Borrow savedBorrow = borrowRepository.save(borrow);

        // Reduce available quantity
        item.setAvailableQuantity(item.getAvailableQuantity() - 1);
        itemRepository.save(item);

        // Create transaction record
        transactionService.createTransaction(savedBorrow.getId(), studentId, item.getId(), "BORROW", "ACTIVE");

        return convertToDTO(savedBorrow);
    }

    // Return Item
    public BorrowDTO returnItem(String borrowId, ReturnBorrowRequest request) {
        Borrow borrow = borrowRepository.findById(borrowId)
                .orElseThrow(() -> new RuntimeException("Borrow record not found"));

        Item item = itemRepository.findById(borrow.getItemId())
                .orElseThrow(() -> new RuntimeException("Item not found"));

        // Update borrow record
        borrow.setReturnDate(LocalDateTime.now());
        borrow.setStatus("RETURNED");
        borrow.setCondition(request.getCondition());
        borrow.setNotes(request.getNotes());
        Borrow updatedBorrow = borrowRepository.save(borrow);

        // Increase available quantity
        item.setAvailableQuantity(item.getAvailableQuantity() + 1);
        itemRepository.save(item);

        // Update transaction
        transactionService.updateTransaction(borrow.getId(), "RETURN", "RETURNED", 
                                            request.getCondition(), request.getNotes());

        return convertToDTO(updatedBorrow);
    }

    // Get Student's Borrows
    public List<BorrowDTO> getStudentBorrows(String studentId) {
        return borrowRepository.findByStudentId(studentId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Get Student's Active Borrows
    public List<BorrowDTO> getStudentActiveBorrows(String studentId) {
        return borrowRepository.findByStudentIdAndStatus(studentId, "ACTIVE").stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Get All Borrows
    public List<BorrowDTO> getAllBorrows() {
        return borrowRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Get Borrow by ID
    public BorrowDTO getBorrowById(String id) {
        return borrowRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new RuntimeException("Borrow not found with id: " + id));
    }

    // Get Overdue Items
    public List<BorrowDTO> getOverdueItems() {
        return borrowRepository.findOverdueItems().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Get Borrows by Item
    public List<BorrowDTO> getBorrowsByItem(String itemId) {
        return borrowRepository.findByItemId(itemId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Helper method to convert Borrow to BorrowDTO
    private BorrowDTO convertToDTO(Borrow borrow) {
        return new BorrowDTO(
                borrow.getId(),
                borrow.getStudentId(),
                borrow.getItemId(),
                borrow.getSerialNumber(),
                borrow.getBorrowDate(),
                borrow.getDueDate(),
                borrow.getReturnDate(),
                borrow.getStatus(),
                borrow.getCondition(),
                borrow.getNotes()
        );
    }
}
