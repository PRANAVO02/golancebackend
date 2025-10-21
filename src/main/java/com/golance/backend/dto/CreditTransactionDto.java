package com.golance.backend.dto;

import java.time.LocalDateTime;

public class CreditTransactionDto {
    private Long userId;   
    private int amount;
    private String type;
    private String description;
    private LocalDateTime timestamp;

    public CreditTransactionDto(Long userId, int amount, String type, String description, LocalDateTime timestamp) {
        this.userId = userId;
        this.amount = amount;
        this.type = type;
        this.description = description;
        this.timestamp = timestamp;
    }

    public Long getUserId() { return userId; }
    public int getAmount() { return amount; }
    public String getType() { return type; }
    public String getDescription() { return description; }
    public LocalDateTime getTimestamp() { return timestamp; }
}

