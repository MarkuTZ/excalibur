package com.example.controllers;

import com.example.models.Task;
import com.example.models.enums.Status;
import com.example.services.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tasks")
public class TaskController {

	private final ProjectService projectService;

	@GetMapping
	public List<Task> getAllTasksForUser() {
		String loggedInEmail = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
		return projectService.getTasks(loggedInEmail);
	}

	@DeleteMapping(value = "/{taskID}")
	public Task deleteTask(@PathVariable("taskID") long taskID) {
		String loggedInEmail = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
		return projectService.deleteTask(taskID, loggedInEmail);
	}

	@PatchMapping(value = "/{taskID}")
	public Task assignUser(@PathVariable("taskID") long taskID, @RequestParam String assignee) {
		String loggedInEmail = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
		return projectService.assignUser(taskID, loggedInEmail, assignee);
	}

	@PatchMapping(value = "/{taskID}/status")
	public Task changeStatus(@PathVariable("taskID") long taskID, @RequestParam Status status) {
		String loggedInEmail = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
		return projectService.changeTaskStatus(taskID, status, loggedInEmail);
	}

}
