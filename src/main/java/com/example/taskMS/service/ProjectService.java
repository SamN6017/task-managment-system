package com.example.taskMS.service;

import com.example.taskMS.dto.ProjectDTO;
import com.example.taskMS.model.Project;
import com.example.taskMS.model.User;
import com.example.taskMS.repository.ProjectRepository;
import com.example.taskMS.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    public ProjectDTO createProject(ProjectDTO projectDTO) {
        User creator = userRepository.findById(projectDTO.getCreatorId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Project project = Project.builder()
                .name(projectDTO.getName())
                .description(projectDTO.getDescription())
                .creator(creator)
                .build();

        Project savedProject = projectRepository.save(project);
        return mapToDTO(savedProject);
    }

    public List<ProjectDTO> getAllProjects() {
        return projectRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private ProjectDTO mapToDTO(Project project) {
        ProjectDTO dto = new ProjectDTO();
        dto.setId(project.getId());
        dto.setName(project.getName());
        dto.setDescription(project.getDescription());
        dto.setCreatorId(project.getCreator().getId());
        return dto;
    }
}