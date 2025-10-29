package com.golance.backend.controller;

import com.golance.backend.dto.BidRequestDto;
import com.golance.backend.dto.BidResponseDto;
import com.golance.backend.model.Bid;
import com.golance.backend.model.Task;
import com.golance.backend.repository.BidRepository;
import com.golance.backend.service.BidService;
import com.golance.backend.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/bids") // All bid endpoints start with /api/bids
public class BidController {

    @Autowired
    private BidService bidService;

    @Autowired
    private BidRepository bidRepository;

    @Autowired
    private TaskService taskService;

    // ------------------ PLACE BID ------------------
    @PostMapping("/tasks/{taskId}")
    public BidResponseDto placeBid(@PathVariable Long taskId, @RequestBody BidRequestDto bidRequestDto) {
        Bid bid = bidService.placeBid(
                taskId,
                bidRequestDto.getUserId(),
                bidRequestDto.getCredits(),
                bidRequestDto.getDescription(),
                bidRequestDto.getEstimatedDays() // pass estimated days
        );
        return mapToResponse(bid);
    }


    // ------------------ GET ALL BIDS FOR A TASK ------------------
    @GetMapping("/tasks/{taskId}")
    public List<BidResponseDto> getBidsForTask(@PathVariable Long taskId) {
        Task task = taskService.getTaskById(taskId);
        return task.getBids().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ------------------ GET SINGLE BID ------------------
    @GetMapping("/{id}")
    public ResponseEntity<BidResponseDto> getBidById(@PathVariable Long id) {
        Optional<Bid> bid = bidRepository.findById(id);
        return bid.map(value -> ResponseEntity.ok(mapToResponse(value)))
                .orElse(ResponseEntity.notFound().build());
    }

    // ------------------ UPDATE BID ------------------
    @PutMapping("/{id}")
    public ResponseEntity<BidResponseDto> updateBid(@PathVariable Long id, @RequestBody BidRequestDto bidRequestDto) {
        Optional<Bid> optionalBid = bidRepository.findById(id);
        if (optionalBid.isPresent()) {
            Bid bid = optionalBid.get();
            bid.setCredits(bidRequestDto.getCredits());
            bid.setDescription(bidRequestDto.getDescription());
            bidRepository.save(bid);
            return ResponseEntity.ok(mapToResponse(bid));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // ------------------ DELETE BID ------------------
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBid(@PathVariable Long id) {
        Optional<Bid> optionalBid = bidRepository.findById(id);
        if (optionalBid.isPresent()) {
            bidRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // ------------------ ALLOCATE TASK TO BID ------------------
    @PostMapping("/tasks/{taskId}/allocate/{bidId}")
    public ResponseEntity<?> allocateTask(@PathVariable Long taskId, @PathVariable Long bidId) {
        try {
            Task task = taskService.allocateTask(taskId, bidId);
            return ResponseEntity.ok(task);
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    // ------------------ GET ALL BIDS BY USER ------------------
    @GetMapping("/user/{userId}")
    public List<BidResponseDto> getBidsByUser(@PathVariable Long userId) {
        List<Bid> bids = bidRepository.findByBidderId(userId);
        return bids.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }


    // ------------------ HELPER: Map Entity -> DTO ------------------
    private BidResponseDto mapToResponse(Bid bid) {
        BidResponseDto dto = new BidResponseDto();
        dto.setId(bid.getId());
        dto.setCredits(bid.getCredits());
        dto.setDescription(bid.getDescription());
        dto.setEstimatedDays(bid.getEstimatedDays());
        if (bid.getBidder() != null) {
            dto.setBidderId(bid.getBidder().getId());
            dto.setBidderName(bid.getBidder().getUsername());
        }
        if (bid.getTask() != null) {
            dto.setTaskId(bid.getTask().getId());
            dto.setTaskTitle(bid.getTask().getTitle());
        }
        if (bid.getBidStatus() != null) {
            dto.setBidStatus(bid.getBidStatus().name());
        }
        return dto;
    }


}