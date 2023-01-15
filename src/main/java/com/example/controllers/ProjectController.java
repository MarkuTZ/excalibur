package com.example.controllers;

import com.example.models.Project;
import com.example.models.Task;
import com.example.models.dto.ProjectDto;
import com.example.models.enums.Status;
import com.example.services.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/projects")
public class ProjectController {

	@Autowired
	private final ProjectService projectService;

	@PostMapping
	public ProjectDto registerProject(@RequestBody ProjectDto projectDto) {
		String loggedInEmail = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
		return projectService.saveInDb(projectDto, loggedInEmail);
	}

	@GetMapping
	public List<ProjectDto> getAllProjects() {
		String loggedInEmail = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
		return projectService.getProjects(loggedInEmail);
	}

	@GetMapping(value = { "/{projectID}" })
	public ProjectDto getProjectById(@PathVariable("projectID") long projectID) {
		String loggedInEmail = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
		return projectService.getProjectById(projectID, loggedInEmail);
	}

	@GetMapping(value = { "/{projectID}/tasks" })
	public ResponseEntity<?> getNumberOfTasksByStatus(@PathVariable("projectID") long projectID,
			@RequestParam(required = false) Status status) {
		if (status != null) {
			return ResponseEntity.ok(projectService.getNumberOfTasksByStatus(projectID, status));
		}
		return ResponseEntity.ok(projectService.getTasks(projectID));
	}

	@GetMapping(value = { "/{projectID}/tasks/{taskID}" })
	public Task getTaskById(@PathVariable("projectID") long projectID, @PathVariable("taskID") long taskID) {
		return projectService.getTaskById(taskID, projectID);
	}

	@GetMapping(value = { "/{projectID}/tasks" })
	public List<Task> getAllTasks(@PathVariable("projectID") long projectID) {
		return projectService.getTasks(projectID);
	}

	@GetMapping(value = { "/{projectID}/tasks/{taskID}" })
	public Task getAllTasks(@PathVariable("projectID") long projectID, @PathVariable("taskID") long taskID) {
		return projectService.getTaskById(taskID, projectID);
	}

	@DeleteMapping(value = { "/{projectID}/tasks/{taskID}" })
	public void deleteTask(@PathVariable("taskID") long taskID) {
		projectService.deleteTask(taskID);
	}

	@DeleteMapping(value = "/{projectID}")
	public Project deleteProject(@PathVariable("projectID") long projectID) {
		String loggedInEmail = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
		return projectService.deleteProject(projectID, loggedInEmail);
	}

	@PostMapping(value = "/{projectID}/collaborators")
	public Project addCollaboratorToProject(@PathVariable("projectID") long projectID,
			@RequestParam String collaboratorEmail) {
		String loggedInEmail = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
		return projectService.addCollaborator(projectID, loggedInEmail, collaboratorEmail);
	}

	@DeleteMapping(value = "/{projectID}/collaborators")
	public Project deleteCollaboratorFromProject(@PathVariable("projectID") long projectID,
			@RequestParam String collaboratorEmail) {
		String loggedInEmail = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
		return projectService.deleteCollaborator(projectID, loggedInEmail, collaboratorEmail);
	}

}
