package com.carddemo.repository;

import com.carddemo.model.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {
    Card findByCardNumber(String cardNumber);
    
    @Query("SELECT c FROM Card c WHERE c.isActive = true")
    List<Card> findAllActive();
}