package com.example.taskMS.service;

import com.example.taskMS.dto.TaskDTO;
import com.example.taskMS.model.Task;
import com.example.taskMS.model.User;
import com.example.taskMS.model.Project;
import com.example.taskMS.model.enums.Status;
import com.example.taskMS.model.enums.Priority;
import com.example.taskMS.repository.TaskRepository;
import com.example.taskMS.repository.UserRepository;
import com.example.taskMS.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;

    public TaskDTO createTask(TaskDTO taskDTO) {
        // 1. Find the Project and User (Assignee)
        Project project = projectRepository.findById(taskDTO.getProjectId())
                .orElseThrow(() -> new RuntimeException("Project not found"));

        User assignee = userRepository.findById(taskDTO.getAssigneeId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 2. Convert DTO to Entity
        Task task = Task.builder()
                .title(taskDTO.getTitle())
                .description(taskDTO.getDescription())
                .status(Status.TODO) // Business Rule: Always start as TODO
                .priority(Priority.valueOf(taskDTO.getPriority()))
                .dueDate(taskDTO.getDueDate())
                .project(project)
                .assignee(assignee)
                .build();

        // 3. Save to DB
        Task savedTask = taskRepository.save(task);

        // 4. Return the Response DTO
        return mapToDTO(savedTask);
    }

    public List<TaskDTO> getAllTasksByProject(Long projectId) {
        return taskRepository.findByProjectId(projectId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // Helper method to convert Entity -> DTO (Prevents Recursion)
    private TaskDTO mapToDTO(Task task) {
        TaskDTO dto = new TaskDTO();
        dto.setId(task.getId());
        dto.setTitle(task.getTitle());
        dto.setDescription(task.getDescription());
        dto.setStatus(task.getStatus().name());
        dto.setPriority(task.getPriority().name());
        dto.setDueDate(task.getDueDate());
        dto.setProjectId(task.getProject().getId());
        dto.setAssigneeId(task.getAssignee().getId());
        dto.setAssigneeName(task.getAssignee().getName());
        return dto;
    }
}