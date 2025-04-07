package com.carddemo.service;

import com.carddemo.model.dto.TransactionRequest;
import com.carddemo.model.Transaction;
import com.carddemo.model.Card;
import com.carddemo.repository.TransactionRepository;
import com.carddemo.exception.CardNotFoundException;
import com.carddemo.exception.TransactionNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TransactionService {
    
    @Autowired
    private TransactionRepository transactionRepository;
    
    @Autowired
    private CardService cardService;

    public Transaction createTransaction(Transaction transaction) {
        Card card = cardService.getCardById(transaction.getCard().getId());
        
        if (!card.getIsActive()) {
            throw new IllegalStateException("Cannot process transaction: Card is not active");
        }
        
        if (!"ACTIVE".equals(card.getStatus())) {
            throw new IllegalStateException("Cannot process transaction: Card status is " + card.getStatus());
        }

        switch (transaction.getType()) {
            case "PURCHASE":
                validatePurchase(transaction, card);
                card.setBalance(card.getBalance() + transaction.getAmount().doubleValue());
                break;
            case "PAYMENT":
                validatePayment(transaction, card);
                card.setBalance(card.getBalance() - transaction.getAmount().doubleValue());
                break;
            default:
                throw new IllegalArgumentException("Invalid transaction type: " + transaction.getType());
        }

        cardService.updateCard(card.getId(), card);
        transaction.setTransactionDate(LocalDateTime.now());
        return transactionRepository.save(transaction);
    }

    private void validatePurchase(Transaction transaction, Card card) {
        if (card.getBalance() + transaction.getAmount().doubleValue() > card.getCreditLimit()) {
            throw new IllegalStateException(
                String.format("Purchase amount %.2f would exceed credit limit. Available credit: %.2f",
                    transaction.getAmount().doubleValue(),
                    card.getCreditLimit() - card.getBalance()));
        }
    }

    private void validatePayment(Transaction transaction, Card card) {
        if (transaction.getAmount().doubleValue() > card.getBalance()) {
            throw new IllegalStateException(
                String.format("Payment amount %.2f exceeds current balance %.2f",
                    transaction.getAmount().doubleValue(),
                    card.getBalance()));
        }
    }

    public Transaction getTransactionById(Long id) {
        return transactionRepository.findById(id)
            .orElseThrow(() -> new TransactionNotFoundException("Transaction not found with id: " + id));
    }

    public List<Transaction> getTransactionsByCardId(Long cardId) {
        Card card = cardService.getCardById(cardId);
        if (card == null) {
            throw new CardNotFoundException("Card not found with id: " + cardId);
        }
        return transactionRepository.findByCardId(cardId);
    }

    public Page<Transaction> getTransactionsByCardId(Long cardId, Pageable pageable) {
        Card card = cardService.getCardById(cardId);
        if (card == null) {
            throw new CardNotFoundException("Card not found with id: " + cardId);
        }
        return transactionRepository.findByCardId(cardId, pageable);
    }

    public List<Transaction> getAllTransactionsWithoutPagination() {
        return transactionRepository.findAll();
    }
    
    public Page<Transaction> getAllTransactions(Pageable pageable) {
        return transactionRepository.findAll(pageable);
    }

    public Transaction updateTransaction(Long id, TransactionRequest request) {
        Transaction existingTransaction = transactionRepository.findById(id)
            .orElseThrow(() -> new TransactionNotFoundException("Transaction not found with id: " + id));
        
        // Update only the fields that are present in the request
        Optional.ofNullable(request.getDescription())
            .ifPresent(existingTransaction::setDescription);
        
        Optional.ofNullable(request.getType())
            .ifPresent(existingTransaction::setType);
        
        return transactionRepository.save(existingTransaction);
    }

    @Transactional
    public void deleteTransaction(Long id) {
        Transaction transaction = transactionRepository.findById(id)
            .orElseThrow(() -> new TransactionNotFoundException("Transaction not found with id: " + id));
        
        transactionRepository.delete(transaction);
    }

    public List<Transaction> generateTransactionReport(
        LocalDate startDate, 
        LocalDate endDate, 
        String type) {
    
        LocalDateTime startDateTime = startDate != null 
            ? startDate.atStartOfDay() 
            : LocalDateTime.MIN;
        
        LocalDateTime endDateTime = endDate != null 
            ? endDate.atTime(23, 59, 59) 
            : LocalDateTime.MAX;
        
        return transactionRepository.findTransactionsByFilter(startDateTime, endDateTime, type);
    }
    
    public Page<Transaction> generateTransactionReport(
        LocalDate startDate, 
        LocalDate endDate, 
        String type,
        Pageable pageable) {
    
        LocalDateTime startDateTime = startDate != null 
            ? startDate.atStartOfDay() 
            : LocalDateTime.MIN;
        
        LocalDateTime endDateTime = endDate != null 
            ? endDate.atTime(23, 59, 59) 
            : LocalDateTime.MAX;
        
        return transactionRepository.findTransactionsByFilterWithPagination(
            startDateTime, endDateTime, type, pageable);
    }
}