package com.golance.backend.service;

import com.golance.backend.dto.TaskResponseDto;
import com.golance.backend.model.Bid;
import com.golance.backend.model.Task;
import com.golance.backend.model.TaskStatus;
import com.golance.backend.repository.BidRepository;
import com.golance.backend.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private BidRepository bidRepository;

    public Task createTask(Task task) {
        task.setStatus(TaskStatus.OPEN);
        return taskRepository.save(task);
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Task getTaskById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));
    }

    public Task updateTask(Long id, Task taskDetails) {
        Task task = getTaskById(id);
        task.setTitle(taskDetails.getTitle());
        task.setDescription(taskDetails.getDescription());
        task.setCreditsOffered(taskDetails.getCreditsOffered());
        task.setCategory(taskDetails.getCategory());
        task.setDeadline(taskDetails.getDeadline());
        task.setStatus(taskDetails.getStatus());
        return taskRepository.save(task);
    }

    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

    // Get tasks posted by a specific user
    public List<Task> getTasksByUser(Long userId) {
        return taskRepository.findByPostedBy_Id(userId);
    }

    // Allocate task to a bid
    public Task allocateTask(Long taskId, Long bidId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        Bid bid = bidRepository.findById(bidId)
                .orElseThrow(() -> new RuntimeException("Bid not found"));

        task.setAssignedUser(bid.getBidder());
        task.setStatus(TaskStatus.ALLOCATED);
        return taskRepository.save(task);
    }

    // Get all tasks assigned to a specific user
    public List<Task> getAssignedTasks(Long userId) {
        return taskRepository.findByAssignedUser_Id(userId);
    }

    // ===========================
    // NEW: Map Task to TaskResponseDto
    // ===========================
    public TaskResponseDto toDto(Task task) {
        TaskResponseDto dto = new TaskResponseDto();
        dto.setId(task.getId());
        dto.setTitle(task.getTitle());
        dto.setDescription(task.getDescription());
        dto.setCategory(task.getCategory());
        dto.setDeadline(task.getDeadline());
        dto.setStatus(task.getStatus().name());
        dto.setCreditsOffered(task.getCreditsOffered());
        dto.setPostedById(task.getPostedBy().getId());
        dto.setPostedByName(task.getPostedBy().getUsername());
        dto.setFilePath(task.getFilePath());
        dto.setFreelancerFilePath(task.getFreelancerFilePath());
        dto.setRating(task.getRating());

        if (task.getAssignedUser() != null) {
            dto.setAssignedUserId(task.getAssignedUser().getId());
            dto.setAssignedUserName(task.getAssignedUser().getUsername());
        }

        return dto;
    }

    public List<TaskResponseDto> getAllTaskDtos() {
        return taskRepository.findAll().stream()
                .map(this::toDto)
                .toList();
    }
}
