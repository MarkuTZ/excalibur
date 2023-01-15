package com.example.models.dto;

import com.example.models.Project;
import com.example.models.Task;
import com.example.models.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDto {

	private long id;

	private String name;

	private String description;

	private Date createDate;

	@JsonFormat(pattern = "yyyy/MM/dd")
	private Date deadline;

	private User owner;

	private Set<Task> tasks;

	private long tasksDone;

	public ProjectDto(Project project) {
		this.id = project.getId();
		this.name = project.getName();
		this.description = project.getDescription();
		this.createDate = project.getCreateDate();
		this.deadline = project.getDeadline();
		this.owner = project.getOwner();
		this.tasks = project.getTasksList();
		this.tasksDone = 0;
	}

}
