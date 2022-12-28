package com.example.controllers;

import com.example.models.Project;
import com.example.models.Task;
import com.example.services.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/projects")
public class ProjectContoller {

	@Autowired
	private final ProjectService projectService;

	@PostMapping
	public Project registerProject(@RequestBody Project project) {
		String loggedInEmail = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
		return projectService.saveInDb(project, loggedInEmail);
	}

	@GetMapping
	public List<Project> getAllProjects() {
		String loggedInEmail = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
		return projectService.getProjects(loggedInEmail);
	}

	@GetMapping(value = { "/{projectID}" })
	public Project getProjectById(@PathVariable("projectID") long projectID) {
		return projectService.getProjectById(projectID);
	}

	@GetMapping(value = { "/{projectID}/tasks" })
	public List<Task> getAllTasks(@PathVariable("projectID") long projectID) {
		return projectService.getTasks(projectID);
	}

	@GetMapping(value = { "/{projectID}/tasks/{taskID}" })
	public Task getAllTasks(@PathVariable("projectID") long projectID, @PathVariable("taskID") long taskID) {
		return projectService.getTaskById(taskID, projectID);
	}
	@DeleteMapping(value = "/{projectID}")
	public void deleteProject(@PathVariable("projectID") long projectID) {
		projectService.deleteProject(projectID);
	}

}
