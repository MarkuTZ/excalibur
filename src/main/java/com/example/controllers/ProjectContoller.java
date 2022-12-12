package com.example.controllers;

import com.example.models.Project;
import com.example.security.JwtUtils;
import com.example.services.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.List;


@RestController
@RequiredArgsConstructor
public class ProjectContoller {

    @Autowired
    private final ProjectService projectService;

    @Autowired
    private final JwtUtils jwtUtils;

    @PostMapping("/projects")
    public Project registerProject(@RequestBody Project project) throws ParseException {
        String loggedInEmail = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        return projectService.saveInDb(project, loggedInEmail);
    }

    @GetMapping("/projects")
    public List<Project> getAllProjects() {
        String loggedInEmail = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        return projectService.getProjects(loggedInEmail);
    }
}
