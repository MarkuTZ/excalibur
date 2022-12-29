package com.example.services;

import com.example.models.Project;
import com.example.models.Task;
import com.example.models.User;
import com.example.repositories.ProjectRepository;
import com.example.repositories.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectService {

    @Autowired
    private final ProjectRepository projectRepository;

    @Autowired
    private final UserService userService;

    @Autowired
    private final TaskRepository taskRepository;

    public List<Project> getProjects(String email) {
        User loggedInUser = userService.getUser(email);
        return projectRepository.findAllByOwnerIs(loggedInUser);
    }

    public Project saveInDb(Project project, String loggedInEmail) {
        User loggedInUser = userService.getUser(loggedInEmail);
        project.setOwner(loggedInUser);
        project.setCreateDate(new Date());
        return projectRepository.save(project);
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
        } else {
            return task;
        }
    }

    public Task saveTaskInDb(Task task, String loggedInEmail, long projectId) {
        System.out.println(task.getName());
        System.out.println(task.getPriority());
//		User loggedUser = userService.getUser(loggedInEmail);
        Project project = projectRepository.findById(projectId).orElse(null);
//		if (!project.getOwner().getUsername().equals(loggedUser.getUsername())) {
//			throw new RuntimeException("Logged in user is not the owner");
//		}
//		task.setId(0L);
//		task.setProject(project);
//		task.setCreateDate(new Date());
//		task.setCreator(loggedUser);
        project.addTask(task);
        return taskRepository.save(task);
    }

}
