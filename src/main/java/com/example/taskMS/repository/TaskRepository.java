package com.example.taskMS.repository;

import com.example.taskMS.model.Task;
import com.example.taskMS.model.enums.Priority;
import com.example.taskMS.model.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    // 1. Existing: Get all tasks for a specific Project board
    List<Task> findByProjectId(Long projectId);

    // 2. Existing: Get "My Tasks" for a specific user
    List<Task> findByAssigneeId(Long userId);

    // 3. New: Filter board tasks by status
    // This is useful for column-specific views on the Kanban board
    List<Task> findByProjectIdAndStatus(Long projectId, Status status);

    // 4. New: Filter board tasks by priority
    // Helps users find "CRITICAL" bugs within a specific project
    List<Task> findByProjectIdAndPriority(Long projectId, Priority priority);

    // 5. New: Find overdue tasks for a specific user
    // Useful for a "Dashboard" feature to show what needs immediate attention
    List<Task> findByAssigneeIdAndStatusNot(Long userId, Status status);
}