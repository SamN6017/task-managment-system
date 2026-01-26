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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;

    public TaskDTO createTask(TaskDTO taskDTO) {
        // 1. Get the currently logged-in user's email from Spring Security
        String currentUserEmail = org.springframework.security.core.context.SecurityContextHolder
                .getContext().getAuthentication().getName();

        User creator = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new RuntimeException("Logged in user not found"));

        // 2. Find the Project and Assignee
        Project project = projectRepository.findById(taskDTO.getProjectId())
                .orElseThrow(() -> new RuntimeException("Project not found"));

        User assignee = userRepository.findById(taskDTO.getAssigneeId())
                .orElseThrow(() -> new RuntimeException("Assignee not found"));

        // 3. Build the Task with the Creator
        Task task = Task.builder()
                .title(taskDTO.getTitle())
                .description(taskDTO.getDescription())
                .status(Status.TODO)
                .priority(Priority.valueOf(taskDTO.getPriority()))
                .dueDate(taskDTO.getDueDate())
                .project(project)
                .assignee(assignee)
                .creator(creator) // <--- THIS WAS THE MISSING LINK
                .build();

        Task savedTask = taskRepository.save(task);
        return mapToDTO(savedTask);
    }

    public List<TaskDTO> getAllTasksByProject(Long projectId) {
        return taskRepository.findByProjectId(projectId)
                .stream()
                .map(this::mapToDTO)
                .collect(toList());
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

    public List<TaskDTO> getMyTasks() {
        // 1. Get the email from the Security Context (JWT)
        String currentUserEmail = org.springframework.security.core.context.SecurityContextHolder
                .getContext().getAuthentication().getName();

        // 2. Find the User
        User user = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 3. Get tasks assigned to this user and map to DTOs
        return taskRepository.findByAssigneeId(user.getId())
                .stream()
                .map(this::mapToDTO)
                .collect(toList());
    }

    public List<TaskDTO> getTeamTasks() {
        // 1. Get the logged-in user
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Task> tasks;

        // 2. Determine Visibility based on Role
        switch (currentUser.getRole()) {
            case CEO:
                // CEO sees EVERYTHING in the company
                tasks = taskRepository.findByAssigneeCompanyId(currentUser.getCompany().getId());
                break;

            case MANAGER:
            case TEAM_LEADER:
                // Leaders see tasks of people reporting to them
                tasks = taskRepository.findByAssigneeReportsToId(currentUser.getId());
                break;

            default:
                // Team Members only see their own tasks
                tasks = taskRepository.findByAssigneeId(currentUser.getId());
                break;
        }

        return tasks.stream().map(this::convertToDTO).toList();
    }

    private TaskDTO convertToDTO(Task task) {
        return TaskDTO.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .status(String.valueOf(task.getStatus()))
                // We only send the name or ID of the assignee, not the whole User object
                .assigneeName(task.getAssignee() != null ? task.getAssignee().getName() : "Unassigned")
                .assigneeId(task.getAssignee() != null ? task.getAssignee().getId() : null)
                .build();
    }


}