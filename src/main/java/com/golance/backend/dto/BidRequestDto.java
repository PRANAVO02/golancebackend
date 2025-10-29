package com.golance.backend.dto;

public class BidRequestDto {
    private Long userId;
    private int credits;
    private String description;
    private int estimatedDays;// <--- NEW FIELD


    // Getters & Setters
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public int getCredits() { return credits; }
    public void setCredits(int credits) { this.credits = credits; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public int getEstimatedDays() { return estimatedDays; }
    public void setEstimatedDays(int estimatedDays) { this.estimatedDays = estimatedDays; }
}
