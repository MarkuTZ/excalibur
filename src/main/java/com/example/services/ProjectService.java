package com.example.services;
import org.modelmapper.ModelMapper;
import com.example.dto.ProjectDto;
import com.example.models.Project;
import com.example.models.Task;
import com.example.models.User;
import com.example.models.enums.Status;
import com.example.repositories.ProjectRepository;
import com.example.repositories.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
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

	@Autowired
	private ModelMapper modelMapper;

	public List<Project> getProjects(String email) {
		User loggedInUser = userService.getUser(email);
		return projectRepository.findAllByOwnerIs(loggedInUser);
	}

	public ProjectDto saveInDb(ProjectDto projectDto, String loggedInEmail) {
		User loggedInUser = userService.getUser(loggedInEmail);

		//convert DTO to entity
		Project project = modelMapper.map(projectDto, Project.class);
		project.setOwner(loggedInUser);
		project.setCreateDate(new Date());

		Project newProject =  projectRepository.save(project);
		//convert entity to DTO
		return modelMapper.map(newProject, ProjectDto.class);

	}

	public Project getProjectById(long id) {
		return projectRepository.findById(id).orElse(null);
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

	public Project deleteProject(long id){
		Project project = projectRepository.findById(id).orElse(null);
		projectRepository.deleteById(id);
		return project;
	}

	public int getNumberOfTasks(long id, Status status){
		return taskRepository.findAllByStatusAndProject_Id(status,id).size();
	}


}
