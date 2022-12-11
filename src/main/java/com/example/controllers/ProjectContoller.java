package com.example.controllers;

import com.example.models.Project;
import com.example.security.JwtUtils;
import com.example.services.ProjectService;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequiredArgsConstructor
public class ProjectContoller {

    @Autowired
    private final ProjectService projectService;

    @Autowired
    private final JwtUtils jwtUtils;

    @PostMapping("/projects")
    public ResponseEntity<?> registerProject(@RequestBody ObjectNode json) throws ParseException {
        String name = json.get("name").textValue();
        String description = json.get("description").textValue();
        String deadLine = json.get("deadline").textValue();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date deadline = df.parse(deadLine);

        Map<String, String> jsonResponse = new HashMap<>();
        if (projectService.existProject(name) == null) {
            System.out.println(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
//            Project project = projectService.saveProjectInDb(name, description, deadline, null);
            return ResponseEntity.ok(null);
        } else {
            jsonResponse.put("Error:", "Project already exist");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(jsonResponse);
        }
    }

    @GetMapping("/projects")
    public List<Project> getAllProjects() {
        String loggedInEmail = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        return projectService.getProjects(loggedInEmail);
    }
}
