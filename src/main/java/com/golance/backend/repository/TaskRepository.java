package com.golance.backend.repository;

import com.golance.backend.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByPostedBy_Id(Long userId);  // NEW
    List<Task> findByAssignedUser_Id(Long assignedUserId);
}
