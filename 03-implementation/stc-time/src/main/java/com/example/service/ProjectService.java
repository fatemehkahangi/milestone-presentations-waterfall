package com.example.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.example.domain.Project;
import com.example.repository.ProjectRepository;

@Service
public class ProjectService {

    private final ProjectRepository projects;

    public ProjectService(ProjectRepository projects) {
        this.projects = projects;
    }

    /** Creates a project. Throws IllegalArgumentException if the name already exists. */
    public Project createProject(String name, String description) {
        throw new UnsupportedOperationException("TODO person 2");
    }

    public List<Project> listProjects() {
        throw new UnsupportedOperationException("TODO person 2");
    }
}