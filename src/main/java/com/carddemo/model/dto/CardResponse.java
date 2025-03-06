package com.carddemo.model.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CardResponse {
    private Long id;
    private String cardNumber;
    private String cardHolder;
    private String expirationDate;
    private Double creditLimit;
    private Double balance;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}