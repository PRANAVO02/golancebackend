package com.golance.backend.controller;

import com.golance.backend.dto.TaskRequestDto;
import com.golance.backend.dto.TaskResponseDto;
import com.golance.backend.dto.StatusUpdateDto;
import com.golance.backend.model.Task;
import com.golance.backend.model.TaskStatus;
import com.golance.backend.model.User;
import com.golance.backend.service.TaskService;
import com.golance.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tasks")
@CrossOrigin(origins = "http://localhost:5173")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private UserService userService;

    // CREATE
    @PostMapping
    public TaskResponseDto createTask(@RequestBody TaskRequestDto taskDto) {
        Task task = new Task();
        task.setTitle(taskDto.getTitle());
        task.setDescription(taskDto.getDescription());
        task.setCategory(taskDto.getCategory());
        task.setDeadline(taskDto.getDeadline());
        task.setCreditsOffered(taskDto.getCreditsOffered());
        task.setStatus(taskDto.getStatus() != null ? Enum.valueOf(TaskStatus.class, taskDto.getStatus()) : TaskStatus.OPEN);

        // Set postedBy
        User postedBy = userService.getUserById(taskDto.getPostedById());
        task.setPostedBy(postedBy);

        // Set assignedUser if provided
        if (taskDto.getAssignedUserId() != null) {
            User assignedUser = userService.getUserById(taskDto.getAssignedUserId());
            task.setAssignedUser(assignedUser);
        }

        Task savedTask = taskService.createTask(task);
        return taskService.toDto(savedTask);
    }

    // READ ALL
    @GetMapping
    public List<TaskResponseDto> getAllTasks() {
        return taskService.getAllTasks().stream()
                .map(taskService::toDto)
                .collect(Collectors.toList());
    }

    // READ BY ID
    @GetMapping("/{id}")
    public TaskResponseDto getTaskById(@PathVariable Long id) {
        Task task = taskService.getTaskById(id);
        return taskService.toDto(task);
    }

    // UPDATE
    @PutMapping("/{id}")
    public TaskResponseDto updateTask(@PathVariable Long id, @RequestBody TaskRequestDto taskDto) {
        Task taskDetails = taskService.getTaskById(id);
        taskDetails.setTitle(taskDto.getTitle());
        taskDetails.setDescription(taskDto.getDescription());
        taskDetails.setCategory(taskDto.getCategory());
        taskDetails.setDeadline(taskDto.getDeadline());
        taskDetails.setCreditsOffered(taskDto.getCreditsOffered());
        taskDetails.setStatus(taskDto.getStatus() != null ? Enum.valueOf(TaskStatus.class, taskDto.getStatus()) : taskDetails.getStatus());

        // Update assignedUser if provided
        if (taskDto.getAssignedUserId() != null) {
            User assignedUser = userService.getUserById(taskDto.getAssignedUserId());
            taskDetails.setAssignedUser(assignedUser);
        }

        Task updatedTask = taskService.updateTask(id, taskDetails);
        return taskService.toDto(updatedTask);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public String deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return "Task deleted successfully!";
    }

    // Get tasks by user
    @GetMapping("/user/{userId}")
    public List<TaskResponseDto> getTasksByUser(@PathVariable Long userId) {
        return taskService.getTasksByUser(userId).stream()
                .map(taskService::toDto)
                .collect(Collectors.toList());
    }

    // Get tasks assigned to user
    @GetMapping("/user/{userId}/assigned-tasks")
    public List<TaskResponseDto> getAssignedTasks(@PathVariable Long userId) {
        return taskService.getAssignedTasks(userId).stream()
                .map(taskService::toDto)
                .collect(Collectors.toList());
    }

    // Update task status
    @PutMapping("/{id}/status")
    public TaskResponseDto updateTaskStatus(@PathVariable Long id, @RequestBody StatusUpdateDto dto) {
        Task task = taskService.getTaskById(id);

        try {
            TaskStatus newStatus = TaskStatus.valueOf(dto.getStatus().toUpperCase());
            task.setStatus(newStatus);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid status: " + dto.getStatus());
        }

        Task updatedTask = taskService.updateTask(id, task);
        return taskService.toDto(updatedTask);
    }
}
