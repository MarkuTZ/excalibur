package com.example.controllers;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/projects")
public class ProjectController {

    @GetMapping()
    public String getAllProjects() {
        return "THIS SHOULD BE A LIST OF PROJECTS";
    }

    @PostMapping()
    public String postProject() {
        // Create Project - Primim date, le validam si le salvam in baza de date
        return null;
    }

    @PutMapping()
    public String putProject() {
        //Update Project
        return null;
    }


    @DeleteMapping()
    public String deleteProject() {
        //Delete project
        return null;
    }


}
