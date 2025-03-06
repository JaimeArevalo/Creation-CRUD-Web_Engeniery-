package com.carddemo.model.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TransactionResponse {
    private Long id;
    private Long cardId;
    private String cardNumber;
    private String cardHolder;
    private BigDecimal amount;
    private String type;
    private String description;
    private LocalDateTime transactionDate;
    private String status;

    public TransactionResponse() {
    }

    // Constructor with all fields
    public TransactionResponse(Long id, Long cardId, String cardNumber, String cardHolder, 
                             BigDecimal amount, String type, String description, 
                             LocalDateTime transactionDate, String status) {
        this.id = id;
        this.cardId = cardId;
        this.cardNumber = cardNumber;
        this.cardHolder = cardHolder;
        this.amount = amount;
        this.type = type;
        this.description = description;
        this.transactionDate = transactionDate;
        this.status = status;
    }

    // Explicit Getters and Setters in case of Lombok failure
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCardId() {
        return cardId;
    }

    public void setCardId(Long cardId) {
        this.cardId = cardId;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardHolder() {
        return cardHolder;
    }

    public void setCardHolder(String cardHolder) {
        this.cardHolder = cardHolder;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // toString() method for debugging
    @Override
    public String toString() {
        return "TransactionResponse{" +
                "id=" + id +
                ", cardId=" + cardId +
                ", cardNumber='" + cardNumber + '\'' +
                ", cardHolder='" + cardHolder + '\'' +
                ", amount=" + amount +
                ", type='" + type + '\'' +
                ", description='" + description + '\'' +
                ", transactionDate=" + transactionDate +
                ", status='" + status + '\'' +
                '}';
    }
}