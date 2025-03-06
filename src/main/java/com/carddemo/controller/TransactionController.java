package com.carddemo.controller;

import com.carddemo.model.Transaction;
import com.carddemo.model.Card;
import com.carddemo.model.dto.TransactionRequest;
import com.carddemo.model.dto.TransactionResponse;
import com.carddemo.service.TransactionService;
import com.carddemo.exception.TransactionNotFoundException;
import com.carddemo.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.Valid;
import java.util.Collections;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
    
    @Autowired
    private TransactionService transactionService;
    
    @Autowired
    private CardService cardService;

    @PostMapping
    public ResponseEntity<?> createTransaction(@Valid @RequestBody TransactionRequest request) {
        try {
            Card card = cardService.getCardById(request.getCardId());
            
            Transaction transaction = new Transaction();
            transaction.setCard(card);
            transaction.setAmount(request.getAmount());
            transaction.setType(request.getType());
            transaction.setDescription(request.getDescription());
            
            Transaction created = transactionService.createTransaction(transaction);
            return ResponseEntity.ok(created);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new ErrorResponse(400, e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        return ResponseEntity.ok(transactionService.getAllTransactions());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTransactionById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(transactionService.getTransactionById(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new ErrorResponse(400, e.getMessage()));
        }
    }

    @GetMapping("/card/{cardId}")
    public ResponseEntity<?> getTransactionsByCardId(@PathVariable Long cardId) {
        try {
            return ResponseEntity.ok(transactionService.getTransactionsByCardId(cardId));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new ErrorResponse(400, e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTransaction(
            @PathVariable Long id, 
            @Valid @RequestBody TransactionRequest request) {
        try {
            Transaction updatedTransaction = transactionService.updateTransaction(id, request);
            return ResponseEntity.ok(updatedTransaction);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new ErrorResponse(400, e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTransaction(@PathVariable Long id) {
        try {
            transactionService.deleteTransaction(id);
            return ResponseEntity.ok(Collections.singletonMap("message", "Transaction deleted successfully"));
        } catch (TransactionNotFoundException e) {
            return ResponseEntity.notFound().build(); // 404 Not Found
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new ErrorResponse(400, "Error deleting transaction: " + e.getMessage()));
        }
    }

    @GetMapping("/report")
    public ResponseEntity<?> generateTransactionReport(
            @RequestParam(required = false) 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) 
            LocalDate startDate,
            
            @RequestParam(required = false) 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) 
            LocalDate endDate,
            
            @RequestParam(required = false) 
            String type) {
        try {
            List<Transaction> report = transactionService.generateTransactionReport(startDate, endDate, type);
            
            // If there are no transactions, return a descriptive message
            if (report.isEmpty()) {
                return ResponseEntity.ok(Collections.singletonMap("message", "No transactions found for the given criteria"));
            }
            
            return ResponseEntity.ok(report);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new ErrorResponse(400, "Error generating transaction report: " + e.getMessage()));
        }
    }

    private static class ErrorResponse {
        private final int status;
        private final String message;

        public ErrorResponse(int status, String message) {
            this.status = status;
            this.message = message;
        }

        public int getStatus() {
            return status;
        }

        public String getMessage() {
            return message;
        }
    }
}