package com.example.controllers;

import com.example.models.Task;
import com.example.models.User;
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

	@DeleteMapping(value = { "/{taskID}" })
	public Task deleteTask(@PathVariable("taskID") long taskID) {
		String loggedInEmail = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
		return projectService.deleteTask(taskID, loggedInEmail);
	}

	@PostMapping(value = { "/{taskID}/assignUser"} )
	public Task assignUser(@PathVariable("taskID") long taskID,@RequestParam String assignedUserEmail){
		String loggedInEmail = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
		return projectService.assignUser(taskID,loggedInEmail,assignedUserEmail);
	}

}
