package com.carddemo.service;

import com.carddemo.model.Card;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

@Service
public class ValidationService {
    
    public List<String> validateCard(Card card) {
        List<String> errors = new ArrayList<>();
        
        // Validate Card Number
        if (card.getCardNumber() == null || !card.getCardNumber().matches("\\d{16}")) {
            errors.add("Card number must be exactly 16 digits");
        }
        
        // Validate Card Holder
        if (card.getCardHolder() == null || !card.getCardHolder().matches("^[a-zA-Z\\s]{2,50}$")) {
            errors.add("Card holder name must be 2-50 characters long and contain only letters and spaces");
        }
        
        // Validate Expiration Date
        if (card.getExpirationDate() != null) {
            if (!card.getExpirationDate().matches("^(0[1-9]|1[0-2])/([2-9]\\d)$")) {
                errors.add("Expiration date must be in MM/YY format");
            } else {
                // Validate that the date is in the future
                try {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yy");
                    YearMonth expiryDate = YearMonth.parse(card.getExpirationDate(), formatter);
                    YearMonth now = YearMonth.now();
                    if (expiryDate.isBefore(now)) {
                        errors.add("Card has expired");
                    }
                } catch (Exception e) {
                    errors.add("Invalid expiration date format");
                }
            }
        }
        
        // Validate CVV
        if (card.getCvv() == null || !card.getCvv().matches("\\d{3}")) {
            errors.add("CVV must be exactly 3 digits");
        }
        
        // Validate Credit Limit
        if (card.getCreditLimit() == null || card.getCreditLimit() <= 0) {
            errors.add("Credit limit must be greater than zero");
        }
        
        // Validate Balance
        if (card.getBalance() == null || card.getBalance() < 0) {
            errors.add("Balance cannot be negative");
        }
        
        // Validate Status
        if (card.getStatus() == null || !isValidStatus(card.getStatus())) {
            errors.add("Status must be either ACTIVE, BLOCKED, or EXPIRED");
        }
        
        return errors;
    }
    
    private boolean isValidStatus(String status) {
        return status.matches("^(ACTIVE|BLOCKED|EXPIRED)$");
    }
    
    public boolean isCardNumberValid(String cardNumber) {
        if (cardNumber == null || cardNumber.length() != 16) {
            return false;
        }
        
        // Luhn Algorithm implementation
        int sum = 0;
        boolean alternate = false;
        
        for (int i = cardNumber.length() - 1; i >= 0; i--) {
            int digit = Character.getNumericValue(cardNumber.charAt(i));
            
            if (alternate) {
                digit *= 2;
                if (digit > 9) {
                    digit -= 9;
                }
            }
            
            sum += digit;
            alternate = !alternate;
        }
        
        return sum % 10 == 0;
    }
}