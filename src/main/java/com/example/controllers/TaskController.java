package com.example.controllers;

import com.example.models.Task;
import com.example.services.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequiredArgsConstructor
public class TaskController {

	@Autowired
	private final TaskService taskService;

	@GetMapping(value = { "/projects/{idProjects}/tasks" })
	@ResponseBody
	public Set<Task> getAllTasks(@PathVariable("idProjects") long projectID) {
		return taskService.getTasks(projectID);
	}

}
