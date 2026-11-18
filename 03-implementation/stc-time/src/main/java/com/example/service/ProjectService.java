package com.example.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.domain.Project;
import com.example.repository.ProjectRepository;

@Service
public class ProjectService {

    private final ProjectRepository projects;

    public ProjectService(ProjectRepository projects) {
        this.projects = projects;
    }

    /** Creates a project. Throws IllegalArgumentException if the name already exists. */
    @Transactional
    public Project createProject(String name, String description) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Project name is required");
        }

        String trimmed = name.trim();
        if (projects.existsByNameIgnoreCase(trimmed)) {
            throw new IllegalArgumentException("Project name already exists");
        }

        Project project = new Project();
        project.setName(trimmed);
        project.setDescription(description);
        return projects.save(project);
    }

    @Transactional(readOnly = true)
    public List<Project> listProjects() {
        return projects.findAll();
    }
}
