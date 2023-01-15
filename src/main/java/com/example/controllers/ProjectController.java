package com.example.controllers;

import com.example.models.Project;
import com.example.models.Task;
import com.example.models.dto.ProjectDto;
import com.example.services.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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

	@PostMapping(value = { "/{projectID}/tasks" })
	public Task createTask(@RequestBody Task task, @PathVariable("projectID") long projectID) {
		String loggedInEmail = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
		return projectService.saveTaskInDb(task, projectID, loggedInEmail);
	}

	@DeleteMapping(value = "/{projectID}")
	public Project deleteProject(@PathVariable("projectID") long projectID) {
		String loggedInEmail = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
		return projectService.deleteProject(projectID, loggedInEmail);
	}

	@PostMapping(value = "/{projectID}/collaborators")
	public Project addCollaboratorToProject(@PathVariable("projectID") long projectID, @RequestParam String email) {
		String loggedInEmail = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
		return projectService.addCollaborator(projectID, loggedInEmail, email);
	}

	@DeleteMapping(value = "/{projectID}/collaborators")
	public Project deleteCollaboratorFromProject(@PathVariable("projectID") long projectID,
			@RequestParam String email) {
		String loggedInEmail = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
		return projectService.deleteCollaborator(projectID, loggedInEmail, email);
	}

}
