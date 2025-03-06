package com.carddemo.service;

import com.carddemo.exception.CardNotFoundException;
import com.carddemo.exception.DuplicateCardException;
import com.carddemo.model.Card;
import com.carddemo.repository.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class CardService {
    
    @Autowired
    private CardRepository cardRepository;
    
    public Card createCard(Card card) {
        validateNewCard(card);
        
        // If no card number is provided, it is automatically generated
        if (card.getCardNumber() == null || card.getCardNumber().trim().isEmpty()) {
            do {
                card.setCardNumber(generateUniqueCardNumber());
            } while (cardRepository.findByCardNumber(card.getCardNumber()) != null);
        } else if (cardRepository.findByCardNumber(card.getCardNumber()) != null) {
            throw new DuplicateCardException("Card number already exists");
        }
        
        // Set default values
        if (card.getBalance() == null) {
            card.setBalance(0.0);
        }
        if (card.getStatus() == null) {
            card.setStatus("ACTIVE");
        }
        card.setIsActive(true);
        
        return cardRepository.save(card);
    }
    
    public List<Card> getAllCards() {
        return cardRepository.findAllActive();
    }
    
    public Page<Card> getAllCards(Pageable pageable) {
        return cardRepository.findAll(pageable);
    }
    
    public Card getCardById(Long id) {
        Card card = cardRepository.findById(id)
            .orElseThrow(() -> new CardNotFoundException("Card not found with id: " + id));
        if (!card.getIsActive()) {
            throw new CardNotFoundException("Card is no longer active");
        }
        return card;
    }
    
    public Card getCardByNumber(String cardNumber) {
        Card card = cardRepository.findByCardNumber(cardNumber);
        if (card == null || !card.getIsActive()) {
            throw new CardNotFoundException("Card not found with number: " + cardNumber);
        }
        return card;
    }
    
    public Card updateCard(Long id, Card cardDetails) {
        Card existingCard = getCardById(id);
        
        if (!existingCard.getIsActive()) {
            throw new IllegalStateException("Cannot update an inactive card");
        }
        
        // Do not allow changing the card number
        cardDetails.setCardNumber(existingCard.getCardNumber());
        
        updateCardFields(existingCard, cardDetails);
        return cardRepository.save(existingCard);
    }

    public Card patchCard(Long id, Map<String, Object> updates) {
        Card card = cardRepository.findById(id)
            .orElseThrow(() -> new CardNotFoundException("Card not found with id: " + id));
        
        if (!card.getIsActive()) {
            throw new IllegalStateException("Cannot update an inactive card");
        }

        if (updates.containsKey("cardHolder")) {
            String cardHolder = (String) updates.get("cardHolder");
            if (cardHolder != null && cardHolder.matches("^[a-zA-Z\\s]{2,50}$")) {
                card.setCardHolder(cardHolder);
            } else {
                throw new IllegalArgumentException("Invalid card holder name");
            }
        }

        if (updates.containsKey("status")) {
            String status = (String) updates.get("status");
            if (status != null && status.matches("^(ACTIVE|BLOCKED|EXPIRED)$")) {
                card.setStatus(status);
            } else {
                throw new IllegalArgumentException("Invalid status");
            }
        }

        if (updates.containsKey("creditLimit")) {
            Double creditLimit = Double.valueOf(updates.get("creditLimit").toString());
            if (creditLimit > 0) {
                card.setCreditLimit(creditLimit);
            } else {
                throw new IllegalArgumentException("Credit limit must be positive");
            }
        }

        if (updates.containsKey("balance")) {
            Double balance = Double.valueOf(updates.get("balance").toString());
            if (balance >= 0) {
                card.setBalance(balance);
            } else {
                throw new IllegalArgumentException("Balance cannot be negative");
            }
        }

        return cardRepository.save(card);
    }
    
    public void deleteCard(Long id) {
        Card card = cardRepository.findById(id)
            .orElseThrow(() -> new CardNotFoundException("Card not found with id: " + id));
        
        if (!card.getIsActive()) {
            throw new IllegalStateException("Card is already inactive");
        }
        
        card.setIsActive(false);
        card.setStatus("EXPIRED");
        cardRepository.save(card);
    }
    
    private void validateNewCard(Card card) {
        if (card.getCreditLimit() == null || card.getCreditLimit() <= 0) {
            throw new IllegalArgumentException("Credit limit must be positive");
        }
    }
    
    private String generateUniqueCardNumber() {
        // Generate a 16-digit card number
        long timestamp = System.currentTimeMillis();
        int random = (int) (Math.random() * 10000);
        return String.format("%016d", (timestamp % 10000000000L) * 10000 + random);
    }
    
    private void updateCardFields(Card existingCard, Card cardDetails) {
        if (cardDetails.getCardHolder() != null) {
            existingCard.setCardHolder(cardDetails.getCardHolder());
        }
        if (cardDetails.getExpirationDate() != null) {
            existingCard.setExpirationDate(cardDetails.getExpirationDate());
        }
        if (cardDetails.getCreditLimit() != null) {
            existingCard.setCreditLimit(cardDetails.getCreditLimit());
        }
        if (cardDetails.getBalance() != null) {
            existingCard.setBalance(cardDetails.getBalance());
        }
        if (cardDetails.getStatus() != null) {
            existingCard.setStatus(cardDetails.getStatus());
        }
    }
}