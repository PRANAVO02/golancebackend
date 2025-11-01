package com.golance.backend.dto;

import java.time.LocalDate;

public class TaskRequestDto {
    private String title;
    private String description;
    private String category;
    private LocalDate deadline;
    private String status; // "PENDING", "OPEN", etc.
    private Integer creditsOffered;
    private Long postedById;
    private Long assignedUserId; // optional
    private String assignedUserName; // add this field
    private String filePath;
    private String freelancerFilePath;
    private Integer rating;


    // Getters & Setters
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public LocalDate getDeadline() { return deadline; }
    public void setDeadline(LocalDate deadline) { this.deadline = deadline; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Integer getCreditsOffered() { return creditsOffered; }
    public void setCreditsOffered(Integer creditsOffered) { this.creditsOffered = creditsOffered; }

    public Long getPostedById() { return postedById; }
    public void setPostedById(Long postedById) { this.postedById = postedById; }

    public String getAssignedUserName() {
        return assignedUserName;
    }

    public void setAssignedUserName(String assignedUserName) {
        this.assignedUserName = assignedUserName;
    }

    public Long getAssignedUserId() { return assignedUserId; }
    public void setAssignedUserId(Long assignedUserId) { this.assignedUserId = assignedUserId; }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFreelancerFilePath() {
        return freelancerFilePath;
    }

    public void setFreelancerFilePath(String freelancerFilePath) {
        this.freelancerFilePath = freelancerFilePath;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }
}