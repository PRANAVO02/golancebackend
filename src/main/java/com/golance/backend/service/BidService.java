package com.golance.backend.service;

import com.golance.backend.model.Bid;
import com.golance.backend.model.BidStatus;
import com.golance.backend.model.Task;
import com.golance.backend.model.User;
import com.golance.backend.repository.BidRepository;
import com.golance.backend.repository.TaskRepository;
import com.golance.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BidService {

    @Autowired
    private BidRepository bidRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    // Place a bid
    public Bid placeBid(Long taskId, Long userId, int credits, String description, int estimatedDays) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (credits > task.getCreditsOffered()) {
            throw new RuntimeException("Credits cannot exceed task's max credits");
        }

        Bid bid = new Bid();
        bid.setTask(task);
        bid.setBidder(user);
        bid.setCredits(credits);
        bid.setDescription(description);
        bid.setEstimatedDays(estimatedDays); // <--- NEW FIELD
        bid.setBidStatus(BidStatus.OPEN);
        return bidRepository.save(bid);
    }

}