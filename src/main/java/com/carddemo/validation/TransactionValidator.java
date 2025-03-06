package com.carddemo.validation;

import com.carddemo.model.Transaction;
import com.carddemo.model.Card;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class TransactionValidator {

    public List<String> validateTransaction(Transaction transaction, Card card) {
        List<String> errors = new ArrayList<>();

        // Validate that the card exists and is active
        if (card == null) {
            errors.add("Card not found");
            return errors;
        }

        if (!"ACTIVE".equals(card.getStatus())) {
            errors.add("Card is not active. Current status: " + card.getStatus());
        }

        // Validate transaction amount
        if (transaction.getAmount() == null || transaction.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            errors.add("Transaction amount must be greater than zero");
        }

        // Validate transaction type
        if (transaction.getType() == null) {
            errors.add("Transaction type is required");
        } else {
            if (!isValidTransactionType(transaction.getType())) {
                errors.add("Invalid transaction type. Must be: PURCHASE, PAYMENT or REFUND");
            }
        }

        // Validate description
        if (transaction.getDescription() == null || transaction.getDescription().trim().isEmpty()) {
            errors.add("Transaction description is required");
        } else if (transaction.getDescription().length() > 100) {
            errors.add("Transaction description cannot exceed 100 characters");
        }

        // Specific validations by transaction type
        if ("PURCHASE".equals(transaction.getType())) {
            validatePurchase(transaction, card, errors);
        } else if ("PAYMENT".equals(transaction.getType())) {
            validatePayment(transaction, card, errors);
        }

        return errors;
    }

    private void validatePurchase(Transaction transaction, Card card, List<String> errors) {
        // Validate credit limit
        if (card.getBalance() + transaction.getAmount().doubleValue() > card.getCreditLimit()) {
            errors.add("Purchase amount would exceed credit limit. Available credit: " + 
                      (card.getCreditLimit() - card.getBalance()));
        }
    }

    private void validatePayment(Transaction transaction, Card card, List<String> errors) {
        // Validate that the payment does not exceed the current balance.
        if (transaction.getAmount().doubleValue() > card.getBalance()) {
            errors.add("Payment amount cannot exceed current balance: " + card.getBalance());
        }
    }

    private boolean isValidTransactionType(String type) {
        return type.matches("^(PURCHASE|PAYMENT|REFUND)$");
    }

    // Additional specific validations
    public boolean isValidRefund(Transaction originalTransaction, Transaction refund) {
        if (!"PURCHASE".equals(originalTransaction.getType())) {
            return false;
        }
        if (refund.getAmount().compareTo(originalTransaction.getAmount()) > 0) {
            return false;
        }
        return true;
    }

    public boolean hasExceededDailyLimit(List<Transaction> dailyTransactions, BigDecimal dailyLimit) {
        BigDecimal total = dailyTransactions.stream()
            .filter(t -> "PURCHASE".equals(t.getType()))
            .map(Transaction::getAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        return total.compareTo(dailyLimit) > 0;
    }
}