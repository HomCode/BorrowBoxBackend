package com.example.BorrowBoxBackend.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.BorrowBoxBackend.dto.BorrowDTO;
import com.example.BorrowBoxBackend.dto.request.BorrowRequest;
import com.example.BorrowBoxBackend.dto.request.ReturnBorrowRequest;
import com.example.BorrowBoxBackend.model.Borrow;
import com.example.BorrowBoxBackend.model.Item;
import com.example.BorrowBoxBackend.repository.BorrowRepository;
import com.example.BorrowBoxBackend.repository.ItemRepository;

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

    public BorrowDTO borrowItem(String studentId, BorrowRequest request) {
        Item item = itemRepository.findById(request.getItemId())
                .orElseThrow(() -> new RuntimeException("Item not found"));

        if (item.getAvailableQuantity() <= 0) {
            throw new RuntimeException("Item is not available for borrowing");
        }

        int days = request.getDays() != null ? request.getDays() : 7;
        LocalDateTime borrowDate = LocalDateTime.now();
        LocalDateTime dueDate = borrowDate.plusDays(days);

        Borrow borrow = new Borrow(
                studentId,
                item.getId(),
                item.getSerialNumber(),
                borrowDate,
                dueDate
        );

        borrow.setNotes(request.getNotes());

        Borrow savedBorrow = borrowRepository.save(borrow);

        item.setAvailableQuantity(item.getAvailableQuantity() - 1);
        itemRepository.save(item);

        transactionService.createTransaction(savedBorrow.getId(), studentId, item.getId(), "BORROW", "ACTIVE");

        return convertToDTO(savedBorrow);
    }

    public BorrowDTO returnItem(String borrowId, ReturnBorrowRequest request) {
        Borrow borrow = borrowRepository.findById(borrowId)
                .orElseThrow(() -> new RuntimeException("Borrow record not found"));

        Item item = itemRepository.findById(borrow.getItemId())
                .orElseThrow(() -> new RuntimeException("Item not found"));

        borrow.setReturnDate(LocalDateTime.now());
        borrow.setStatus("RETURNED");
        borrow.setCondition(request.getCondition());
        borrow.setNotes(request.getNotes());

        Borrow updatedBorrow = borrowRepository.save(borrow);

        item.setAvailableQuantity(item.getAvailableQuantity() + 1);
        itemRepository.save(item);

        transactionService.updateTransaction(
                borrow.getId(),
                "RETURN",
                "RETURNED",
                request.getCondition(),
                request.getNotes()
        );

        return convertToDTO(updatedBorrow);
    }

    public List<BorrowDTO> getStudentBorrows(String studentId) {
        return borrowRepository.findByStudentId(studentId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<BorrowDTO> getStudentActiveBorrows(String studentId) {
        return borrowRepository.findByStudentIdAndStatus(studentId, "ACTIVE").stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<BorrowDTO> getAllBorrows() {
        return borrowRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public BorrowDTO getBorrowById(String id) {
        return borrowRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new RuntimeException("Borrow not found with id: " + id));
    }

    public List<BorrowDTO> getOverdueItems() {
        return borrowRepository.findOverdueItems().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<BorrowDTO> getBorrowsByItem(String itemId) {
        return borrowRepository.findByItemId(itemId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

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