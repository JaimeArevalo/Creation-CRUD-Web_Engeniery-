package com.carddemo.validation;

import com.carddemo.model.Card;
import com.carddemo.service.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CardValidator {
    
    @Autowired
    private ValidationService validationService;

    public void validateCardCreation(Card card) {
        List<String> errors = validationService.validateCard(card);
        
        if (!errors.isEmpty()) {
            throw new IllegalArgumentException(String.join(", ", errors));
        }
        
        // Additional validation for card creation
        if (!validationService.isCardNumberValid(card.getCardNumber())) {
            throw new IllegalArgumentException("Invalid card number (failed Luhn check)");
        }
    }
    
    public void validateCardUpdate(Card card) {
        List<String> errors = validationService.validateCard(card);
        
        if (!errors.isEmpty()) {
            throw new IllegalArgumentException(String.join(", ", errors));
        }
    }
    
    public void validatePartialUpdate(Card card) {
        // Validate only the fields that are present
        List<String> errors = validationService.validateCard(card);
        
        if (!errors.isEmpty()) {
            throw new IllegalArgumentException(String.join(", ", errors));
        }
    }
    
    public boolean isValidForSearch(String cardNumber) {
        return cardNumber != null && cardNumber.matches("\\d{16}");
    }
}