package com.golance.backend.dto;

import java.time.LocalDate;
import java.util.List;

public class TaskResponseDto {
    private Long id;
    private String title;
    private String description;
    private String category;
    private LocalDate deadline;
    private String status;
    private int creditsOffered;
    private Long postedById;
    private Long assignedUserId;
    private List<BidResponseDto> bids;
    private String assignedUserName; // add this
    private String postedByName;
    private String filePath;
    private String freelancerFilePath;

    // getters & setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getCreditsOffered() {
        return creditsOffered;
    }

    public void setCreditsOffered(int creditsOffered) {
        this.creditsOffered = creditsOffered;
    }

    public Long getPostedById() {
        return postedById;
    }

    public void setPostedById(Long postedById) {
        this.postedById = postedById;
    }

    public String getPostedByName() {
        return postedByName;
    }

    public void setPostedByName(String postedByName) {
        this.postedByName = postedByName;
    }

    public Long getAssignedUserId() {
        return assignedUserId;
    }

    public void setAssignedUserId(Long assignedUserId) {
        this.assignedUserId = assignedUserId;
    }

    public String getAssignedUserName() {
        return assignedUserName;
    }

    public void setAssignedUserName(String assignedUserName) {
        this.assignedUserName = assignedUserName;
    }

    public List<BidResponseDto> getBids() {
        return bids;
    }

    public void setBids(List<BidResponseDto> bids) {
        this.bids = bids;
    }

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
}
