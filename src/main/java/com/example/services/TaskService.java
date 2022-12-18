package com.example.services;

import com.example.models.Project;
import com.example.models.Task;
import com.example.repositories.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    @Autowired
    private final TaskRepository taskRepository;
    @Autowired
    private final ProjectService projectService;

    public List<Task> getTasks(long id) {
        Project project = projectService.getProjectById(id);
        return project.getTasksList();
    }

//    public Task getTask(int idTask,int idProject){
//
//    }

}
