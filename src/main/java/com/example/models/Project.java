package com.example.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.*;

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
	private User owner;

	@JsonManagedReference
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "project")
	private Set<Task> tasksList;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "project_user_collaborators",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> collaborators = new HashSet<>();

    //TODO: Create methods for adding tasks
    // ex: addTask(Task task) should add the task to the list of tasks and then set the project as the task's project.
}
