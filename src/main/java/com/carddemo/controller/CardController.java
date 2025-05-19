package com.carddemo.controller;

import com.carddemo.model.Card;
import com.carddemo.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public ResponseEntity<Map<String, Object>> getAllCards(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        
        Sort.Direction direction = sortDir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        
        Page<Card> pageCards = cardService.getAllCards(pageable);
        
        Map<String, Object> response = new HashMap<>();
        response.put("content", pageCards.getContent());
        response.put("currentPage", pageCards.getNumber());
        response.put("totalItems", pageCards.getTotalElements());
        response.put("totalPages", pageCards.getTotalPages());
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/all")
    public ResponseEntity<List<Card>> getAllCardsWithoutPagination() {
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