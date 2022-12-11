package com.example.services;

import com.example.models.Project;
import com.example.models.User;
import com.example.repositories.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class ProjectService {

    @Autowired
    private final ProjectRepository projectRepository;

    public Project existProject(String name) {
        return projectRepository.findByName(name);
    }

    public Project saveProjectInDb(String name, String description, Date deadline, User owner) {
        Project project= new Project();
        if (existProject(name) == null) {
            project.setName(name);
            project.setDescription(description);
            project.setOwner(owner);
            project.setCreate_date(new Date());
            project.setDeadline(deadline);
            return projectRepository.save(project);
        } else {
            return null;
        }
    }
}
