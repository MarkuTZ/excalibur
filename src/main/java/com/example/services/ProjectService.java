package com.example.services;

import com.example.exception.GenericError;
import com.example.models.Project;
import com.example.models.Task;
import com.example.models.User;
import com.example.models.dto.ProjectDto;
import com.example.models.enums.Status;
import com.example.repositories.ProjectRepository;
import com.example.repositories.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectService {

	@Autowired
	private final ProjectRepository projectRepository;

	@Autowired
	private final TaskRepository taskRepository;

	@Autowired
	private final UserService userService;

	public List<ProjectDto> getProjects(String email) {
		User loggedInUser = userService.getUser(email);
		List<Project> projects = projectRepository.findAllByOwnerIs(loggedInUser);
		return projects.stream().map(project -> {
			ProjectDto projectDto = new ProjectDto(project);
			projectDto.setTasksDone(
					project.getTasksList().stream().filter(task -> task.getStatus().equals(Status.DONE)).count());
			return projectDto;
		}).collect(Collectors.toList());
	}

	public ProjectDto saveInDb(ProjectDto projectDto, String loggedInEmail) {
		User loggedInUser = userService.getUser(loggedInEmail);

		// convert DTO to entity
		Project project = new Project(projectDto);
		project.setOwner(loggedInUser);
		project.setCreateDate(new Date());

		return new ProjectDto(projectRepository.save(project));
	}

	public ProjectDto getProjectById(long id, String loggedInEmail) {
		User loggedUser = userService.getUser(loggedInEmail);
		Project project = projectRepository.findById(id).orElse(null);
		if (project == null) {
			throw new GenericError(HttpStatus.NOT_FOUND, "Project with id:" + id + " doesn't exist!");
		}
		// TODO: Add collaborators as well
		else if (!project.getOwner().getUsername().equals(loggedUser.getUsername())) {
			throw new GenericError(HttpStatus.BAD_REQUEST,
					"Project with id:" + id + " was not created by " + loggedUser.getUsername());
		}
		ProjectDto projectDto = new ProjectDto(project);
		projectDto.setTasksDone(
				project.getTasksList().stream().filter(task -> task.getStatus().equals(Status.DONE)).count());
		return projectDto;
	}

	public List<Task> getTasks(long project_id) {
		return taskRepository.findAllByProject_Id(project_id);
	}

	public Task getTaskById(long taskID, long projectID) {
		Task task = taskRepository.findById(taskID).orElse(null);
		if (task == null || task.getProject().getId() != projectID) {
			return null;
		}
		else {
			return task;
		}
	}

	public Project deleteProject(long projectID, String loggedInEmail) {
		User owner = userService.getUser(loggedInEmail);
		Project project = projectRepository.findById(projectID).orElse(null);
		if (!Objects.equals(project.getOwner().getUsername(), owner.getUsername())) {
			throw new GenericError(HttpStatus.UNAUTHORIZED, "Only the owner can delete the project");
		}
		projectRepository.deleteById(projectID);
		return project;
	}

	public int getNumberOfTasksByStatus(long id, Status status) {
		return taskRepository.findAllByStatusAndProject_Id(status, id).size();
	}

	public Project addCollaborator(long projectID, String loggedInEmail, String collaboratorEmail) {
		User owner = userService.getUser(loggedInEmail);
		Project project = projectRepository.findById(projectID).orElse(null);
		if (!Objects.equals(project.getOwner().getUsername(), owner.getUsername())) {
			throw new GenericError(HttpStatus.UNAUTHORIZED, "Only the owner can add collaborators to the project");
		}
		User collaborator = userService.getUser(collaboratorEmail);
		project.addCollaborator(collaborator);
		return projectRepository.save(project);
	}

	public Project deleteCollaborator(long projectID, String loggedInEmail, String collaboratorEmail) {
		User owner = userService.getUser(loggedInEmail);
		Project project = projectRepository.findById(projectID).orElse(null);
		if (!Objects.equals(project.getOwner().getUsername(), owner.getUsername())) {
			throw new GenericError(HttpStatus.UNAUTHORIZED, "Only the owner can remove collaborators from the project");
		}
		User collaborator = userService.getUser(collaboratorEmail);
		project.deleteCollaborator(collaborator);
		return projectRepository.save(project);
	}

}
