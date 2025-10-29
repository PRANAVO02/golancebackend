package com.golance.backend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
@Table(name = "bids")
public class Bid {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "task_id")
    @JsonBackReference
    private Task task;

    @ManyToOne
    @JoinColumn(name = "bidder_id")
    private User bidder;

    private int credits;

    private int estimatedDays;

    @Enumerated(EnumType.STRING)
    private BidStatus bidStatus = BidStatus.OPEN; // default OPEN

    private String description;

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Task getTask() { return task; }
    public void setTask(Task task) { this.task = task; }

    public User getBidder() { return bidder; }
    public void setBidder(User bidder) { this.bidder = bidder; }

    public int getCredits() { return credits; }
    public void setCredits(int credits) { this.credits = credits; }

    public int getEstimatedDays() {
        return estimatedDays;
    }

    public void setEstimatedDays(int estimatedDays) {
        this.estimatedDays = estimatedDays;
    }

    public BidStatus getBidStatus() {
        return bidStatus;
    }

    public void setBidStatus(BidStatus bidStatus) {
        this.bidStatus = bidStatus;
    }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
