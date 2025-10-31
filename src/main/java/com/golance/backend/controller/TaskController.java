package com.golance.backend.controller;

import com.golance.backend.dto.TaskRequestDto;
import com.golance.backend.dto.TaskResponseDto;
import com.golance.backend.dto.StatusUpdateDto;
import com.golance.backend.model.Task;
import com.golance.backend.model.TaskStatus;
import com.golance.backend.model.User;
import com.golance.backend.repository.TaskRepository;
import com.golance.backend.service.TaskService;
import com.golance.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.nio.file.*;
import java.nio.file.StandardCopyOption; // ✅ Needed

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tasks")
//@CrossOrigin(origins = "http://localhost:5173")
public class TaskController {

    private static final String UPLOAD_DIR = "uploads/";
    private static final String FREELANCER_UPLOAD_DIR = "uploads/freelancers/";

    @Autowired
    private TaskService taskService;

    @Autowired
    private UserService userService;

    @Autowired
    private TaskRepository taskRepository;


//     CREATE
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

    // --- POST Task with File Upload ---
    @PostMapping(consumes = {"multipart/form-data"})
    public TaskResponseDto createTask(
            @RequestPart("task") TaskRequestDto taskDto,
            @RequestPart(value = "file", required = false) MultipartFile file
    ) {
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

        // Handle file upload
        if (file != null && !file.isEmpty()) {
            try {
                Files.createDirectories(Paths.get(UPLOAD_DIR));
                String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                Path filePath = Paths.get(UPLOAD_DIR + filename);
                Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                task.setFilePath(filename);
            } catch (Exception e) {
                throw new RuntimeException("File upload failed: " + e.getMessage());
            }
        }

        Task savedTask = taskService.createTask(task);
        return taskService.toDto(savedTask);
    }

    @GetMapping("/download/{taskId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        if (task.getFilePath() == null) {
            return ResponseEntity.notFound().build();
        }

        try {
            Path filePath = Paths.get(UPLOAD_DIR).resolve(task.getFilePath()).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists()) {
                return ResponseEntity.notFound().build();
            }

            String contentType = Files.probeContentType(filePath);
            if (contentType == null) {
                contentType = "application/octet-stream";
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
    @PostMapping("/upload/freelancer/{taskId}")
    public ResponseEntity<String> uploadFreelancerFile(
            @PathVariable Long taskId,
            @RequestParam("file") MultipartFile file
    ) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("❌ File is empty");
            }

            Files.createDirectories(Paths.get(FREELANCER_UPLOAD_DIR));

            String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(FREELANCER_UPLOAD_DIR + filename);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            Task task = taskRepository.findById(taskId)
                    .orElseThrow(() -> new RuntimeException("Task not found"));

            // ✅ Save freelancer file path
            task.setFreelancerFilePath(filename);
            taskRepository.save(task);

            return ResponseEntity.ok("✅ Freelancer file uploaded successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body("❌ File upload failed: " + e.getMessage());
        }
    }

    @GetMapping("/download/freelancer/{taskId}")
    public ResponseEntity<Resource> downloadFreelancerFile(@PathVariable Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        if (task.getFreelancerFilePath() == null) {
            return ResponseEntity.notFound().build();
        }

        try {
            Path filePath = Paths.get(FREELANCER_UPLOAD_DIR).resolve(task.getFreelancerFilePath()).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists()) {
                return ResponseEntity.notFound().build();
            }

            String contentType = Files.probeContentType(filePath);
            if (contentType == null) {
                contentType = "application/octet-stream";
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
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
    @PutMapping("/{taskId}/rate")
    public ResponseEntity<Task> rateTask(
            @PathVariable Long taskId,
            @RequestBody Map<String, Integer> request) {

        int rating = request.get("rating");

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        // ✅ TaskStatus is ENUM, so compare directly
        if (task.getStatus() != TaskStatus.COMPLETED) {
            return ResponseEntity.badRequest().body(null);
        }

        // ✅ Save the rating for this task
        task.setRating(rating);
        taskRepository.save(task);

        // ✅ Update assigned user's overall rating
        if (task.getAssignedUser() != null) {
            userService.updateUserRating(task.getAssignedUser().getId(), rating);
        }

        return ResponseEntity.ok(task);
    }

}
