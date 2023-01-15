package com.example.models;

import com.example.models.dto.ProjectDto;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Table(name = "project")
@Entity
@Getter
@Setter
@NoArgsConstructor
public class Project {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column
	private String name;

	@Column
	private String description;

	@Column
	private Date createDate;

	@Column
	private Date deadline;

	@ManyToOne
	@NotNull
	private User owner;

	@JsonManagedReference
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "project")
	private Set<Task> tasksList;

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "project_user_collaborators", joinColumns = @JoinColumn(name = "project_id"),
			inverseJoinColumns = @JoinColumn(name = "user_id"))
	private Set<User> collaborators;

	public Project(ProjectDto projectDto) {
		this.name = projectDto.getName();
		this.description = projectDto.getDescription();
		this.deadline = projectDto.getDeadline();
	}

	public void addCollaborator(User collaborator) {
		collaborators.add(collaborator);
	}

	public void deleteCollaborator(User collaborator) {
		collaborators.remove(collaborator);
	}

	public void addTask(Task task) {
		tasksList.add(task);
	}

}
