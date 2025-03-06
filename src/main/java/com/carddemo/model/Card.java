package com.carddemo.model;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "cards")
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, length = 16)
    @Pattern(regexp = "\\d{16}", message = "Card number must be exactly 16 digits")
    private String cardNumber;
    
    @NotBlank(message = "Card holder name is required")
    @Pattern(regexp = "^[a-zA-Z\\s]{2,50}$", message = "Card holder name must be 2-50 characters long and contain only letters and spaces")
    private String cardHolder;
    
    @Pattern(regexp = "^(0[1-9]|1[0-2])/([2-9]\\d)$", message = "Expiration date must be in MM/YY format")
    private String expirationDate;
    
    @Pattern(regexp = "\\d{3}", message = "CVV must be exactly 3 digits")
    private String cvv;
    
    @NotNull(message = "Credit limit is required")
    @Positive(message = "Credit limit must be positive")
    private Double creditLimit;
    
    @PositiveOrZero(message = "Balance must be zero or positive")
    private Double balance = 0.0;
    
    @NotNull(message = "Status is required")
    @Pattern(regexp = "^(ACTIVE|BLOCKED|EXPIRED)$", message = "Status must be either ACTIVE, BLOCKED, or EXPIRED")
    private String status = "ACTIVE";
    
    @CreationTimestamp
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    
    @Column(nullable = false)
    private Boolean isActive = true;

    @PrePersist
    public void prePersist() {
        if (this.balance == null) {
            this.balance = 0.0;
        }
        if (this.status == null) {
            this.status = "ACTIVE";
        }
        if (this.cardNumber == null) {
            this.cardNumber = generateCardNumber();
        }
    }

    private String generateCardNumber() {
        // Generates a unique 16-digit card number
        return String.format("%016d", System.nanoTime() % 10000000000000000L);
    }

    // Getter and Setter for id
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getIsActive() {
        return isActive;
    }
    
    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    // Explicit Getters and Setters in case Lombok fails
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

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public Double getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(Double creditLimit) {
        this.creditLimit = creditLimit;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}