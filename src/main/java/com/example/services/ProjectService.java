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
	private final UserService userService;

	@Autowired
	private final TaskRepository taskRepository;

	public List<ProjectDto> getProjects(String email) {
		User loggedInUser = userService.getUser(email);
		List<Project> projects = projectRepository.findAll();
		projects = projects.stream().filter(
				project -> project.getOwner().equals(loggedInUser) || project.getCollaborators().contains(loggedInUser))
				.collect(Collectors.toList());
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

	public ProjectDto getProjectById(long projectId, String loggedInEmail) {
		User loggedUser = userService.getUser(loggedInEmail);
		Project project = projectRepository.findById(projectId).orElse(null);
		if (project == null) {
			throw new GenericError(HttpStatus.NOT_FOUND, "Project with id:" + projectId + " doesn't exist!");
		}
		// TODO: Add collaborators as well
		else if (!project.getOwner().getUsername().equals(loggedUser.getUsername())
				&& !project.getCollaborators().contains(loggedUser)) {
			throw new GenericError(HttpStatus.BAD_REQUEST,
					loggedUser.getUsername() + " doesn't have acces to project with id: " + projectId);
		}
		ProjectDto projectDto = new ProjectDto(project);
		projectDto.setTasksDone(
				project.getTasksList().stream().filter(task -> task.getStatus().equals(Status.DONE)).count());
		return projectDto;
	}

	public List<Task> getTasks(String userEmail) {
		return taskRepository.findAllByAssignee_Username(userEmail);
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

	public Task saveTaskInDb(Task task, long projectId, String loggedInEmail) {
		User user = userService.getUser(loggedInEmail);
		Project project = projectRepository.findById(projectId).orElse(null);
		if (project == null) {
			throw new GenericError(HttpStatus.NOT_FOUND, "Project was not found");
		}
		if (!project.getOwner().getUsername().equals(user.getUsername())
				&& !project.getCollaborators().contains(user)) {
			throw new GenericError(HttpStatus.UNAUTHORIZED, "Only the owner and collaborators can create tasks");
		}
		task.setId(0L);
		task.setProject(project);
		task.setCreateDate(new Date());
		task.setCreator(user);
		project.addTask(task);
		return taskRepository.save(task);
	}

	public Task deleteTask(long idTask, String loggedInEmail) {
		User owner = userService.getUser(loggedInEmail);
		Task task = taskRepository.findById(idTask).orElse(null);
		if (task == null) {
			throw new GenericError(HttpStatus.NOT_FOUND, "Task was not found");
		}
		if (!Objects.equals(task.getCreator().getUsername(), owner.getUsername())) {
			throw new GenericError(HttpStatus.UNAUTHORIZED, "Only the owner can delete tasks");
		}
		taskRepository.delete(task);
		return task;

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

	public Task assignUser(long taskID, String loggedInEmail, String assignedUserEmail) {
		User owner = userService.getUser(loggedInEmail);
		Task task = taskRepository.findById(taskID).orElse(null);
		if (task == null) {
			throw new GenericError(HttpStatus.NOT_FOUND, "Task was not found");
		}
		if (!Objects.equals(task.getCreator().getUsername(), owner.getUsername())) {
			throw new GenericError(HttpStatus.UNAUTHORIZED, "Only the owner can assign users to a task");
		}
		User assignedUser = userService.getUser(assignedUserEmail);
		task.setAssignee(assignedUser);
		return taskRepository.save(task);
	}

	public Task changeTaskStatus(long taskID, Status status, String loggedInEmail) {
		User loggedUser = userService.getUser(loggedInEmail);
		Task task = taskRepository.findById(taskID).orElse(null);
		if (task == null) {
			throw new GenericError(HttpStatus.NOT_FOUND, "Task was not found");
		}
		if (!task.getCreator().equals(loggedUser) && !task.getAssignee().equals(loggedUser)) {
			throw new GenericError(HttpStatus.UNAUTHORIZED, "Only the creator or the assignee can change the status");
		}
		task.setStatus(status);
		return taskRepository.save(task);
	}

}
