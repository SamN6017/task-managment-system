package com.example.taskMS.repository;

import com.example.taskMS.model.Task;
import com.example.taskMS.model.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    // Get all tasks for a specific Project board
    List<Task> findByProjectId(Long projectId);

    // Get "My Tasks" for a specific user
    List<Task> findByAssigneeId(Long userId);

    // Filter board by status (e.g., show all 'IN_PROGRESS' tasks)
    List<Task> findByStatus(Status status);
}