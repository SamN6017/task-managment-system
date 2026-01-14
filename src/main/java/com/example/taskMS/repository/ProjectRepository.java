package com.example.taskMS.repository;

import com.example.taskMS.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    // Get all projects created by a specific user (Manager view)
    List<Project> findByCreatorId(Long userId);
}