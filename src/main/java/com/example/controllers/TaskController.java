package com.example.controllers;


import com.example.models.Task;
import com.example.services.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TaskController {


    @Autowired
    private final TaskService taskService;


    @GetMapping(value = {"/projects/{idProjects}/tasks"})
    @ResponseBody
    public List<Task> getAllTasks(@PathVariable("idProjects") String projectID) {

        int IdProject = Integer.parseInt(projectID);
        return taskService.getTasks(IdProject);
    }
}
