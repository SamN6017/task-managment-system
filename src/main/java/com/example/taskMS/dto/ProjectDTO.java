package com.example.taskMS.dto;

import lombok.Data;

@Data
public class ProjectDTO {
    private Long id;
    private String name;
    private String description;
    private Long creatorId;
}