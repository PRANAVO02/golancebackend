package com.golance.backend.controller;

import com.golance.backend.dto.TaskRequestDto;
import com.golance.backend.model.Task;
import com.golance.backend.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    // Create Task
    @PostMapping("/create")
    public Task createTask(@RequestBody TaskRequestDto dto) {
        return taskService.createTask(dto);
    }

    // Get All Tasks
    @GetMapping("/all")
    public List<Task> getAllTasks() {
        return taskService.getAllTasks();
    }
}
