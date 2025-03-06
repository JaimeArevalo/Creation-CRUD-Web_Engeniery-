package com.carddemo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionRequest {
    private Long cardId;

    @Positive(message = "Amount must be positive")
    private BigDecimal amount;

    @Size(min = 3, max = 20, message = "Transaction type must be between 3 and 20 characters")
    private String type;

    @Size(max = 100, message = "Description cannot exceed 100 characters")
    private String description;

    // Explicitly define getters to ensure they are available
    public Long getCardId() {
        return cardId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }
}