package com.example.services;

import com.example.models.Project;
import com.example.models.User;
import com.example.repositories.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectService {

    @Autowired
    private final ProjectRepository projectRepository;
    @Autowired
    private final UserService userService;

    public List<Project> getProjects(String email) {
        User loggedInUser = userService.getUser(email);
        return projectRepository.findAllByOwnerIs(loggedInUser);
    }

    public Project saveInDb(Project project, String loggedInEmail) {
        User loggedInUser = userService.getUser(loggedInEmail);
        project.setOwner(loggedInUser);
        project.setCreateDate(new Date());
        return projectRepository.save(project);
    }

    public Project getProjectById(long id) {

        return projectRepository.findById(id).orElse(null);
    }
}
