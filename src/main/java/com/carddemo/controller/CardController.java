package com.carddemo.controller;

import com.carddemo.model.Card;
import com.carddemo.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/cards")
@Validated
public class CardController {
    
    @Autowired
    private CardService cardService;
    
    @PostMapping
    public ResponseEntity<Card> createCard(@Valid @RequestBody Card card) {
        return ResponseEntity.ok(cardService.createCard(card));
    }
    
    @GetMapping
    public ResponseEntity<List<Card>> getAllCards() {
        return ResponseEntity.ok(cardService.getAllCards());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Card> getCardById(@PathVariable Long id) {
        return ResponseEntity.ok(cardService.getCardById(id));
    }
    
    @GetMapping("/number/{cardNumber}")
    public ResponseEntity<Card> getCardByNumber(@PathVariable String cardNumber) {
        return ResponseEntity.ok(cardService.getCardByNumber(cardNumber));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Card> updateCard(@PathVariable Long id, @Valid @RequestBody Card cardDetails) {
        return ResponseEntity.ok(cardService.updateCard(id, cardDetails));
    }
    
    @PatchMapping("/{id}")
    public ResponseEntity<?> patchCard(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        try {
            Card updatedCard = cardService.patchCard(id, updates);
            return ResponseEntity.ok(updatedCard);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", 400);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCard(@PathVariable Long id) {
        try {
            cardService.deleteCard(id);
            return ResponseEntity.ok()
                .body(Map.of(
                    "message", "Card successfully deactivated",
                    "cardId", id
                ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of(
                    "error", e.getMessage(),
                    "cardId", id
                ));
        }
    }
}