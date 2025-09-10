package com.golance.backend.service;

import com.golance.backend.dto.TaskRequestDto;
import com.golance.backend.model.Task;
import com.golance.backend.model.TaskStatus;
import com.golance.backend.model.User;
import com.golance.backend.repository.TaskRepository;
import com.golance.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    // Create Task
    public Task createTask(TaskRequestDto dto) {
        User user = userRepository.findById(dto.getPostedById())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Task task = new Task();
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setCategory(dto.getCategory());
        task.setDeadline(dto.getDeadline());
        task.setCreditsOffered(dto.getCreditsOffered());
        task.setPostedBy(user);

        // convert status string to enum
        if(dto.getStatus() != null){
            task.setStatus(TaskStatus.valueOf(dto.getStatus()));
        } else {
            task.setStatus(TaskStatus.OPEN); // default
        }

        return taskRepository.save(task);
    }

    // Get All Tasks
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }
}
