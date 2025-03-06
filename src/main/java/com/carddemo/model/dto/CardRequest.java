package com.carddemo.model.dto;

import lombok.Data;
import javax.validation.constraints.*;

@Data
public class CardRequest {
    @Pattern(regexp = "\\d{16}", message = "Card number must be exactly 16 digits")
    private String cardNumber;

    @NotBlank(message = "Card holder name is required")
    @Pattern(regexp = "^[a-zA-Z\\s]{2,50}$", message = "Card holder name must be 2-50 characters long and contain only letters and spaces")
    private String cardHolder;

    @Pattern(regexp = "^(0[1-9]|1[0-2])/([2-9]\\d)$", message = "Expiration date must be in MM/YY format and be a future date")
    private String expirationDate;

    @Pattern(regexp = "\\d{3}", message = "CVV must be exactly 3 digits")
    private String cvv;

    @NotNull(message = "Credit limit is required")
    @Positive(message = "Credit limit must be positive")
    private Double creditLimit;

    @PositiveOrZero(message = "Balance must be zero or positive")
    private Double balance = 0.0;

    @Pattern(regexp = "^(ACTIVE|BLOCKED|EXPIRED)$", message = "Status must be either ACTIVE, BLOCKED, or EXPIRED")
    private String status = "ACTIVE";
}