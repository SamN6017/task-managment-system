package com.example.taskMS.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class TaskDTO {
    private Long id;
    private String title;
    private String description;
    private String status;
    private String priority;
    private LocalDateTime dueDate;
    private Long projectId;
    private Long assigneeId;
    private String assigneeName; // Helps the frontend display names without extra API calls
}