package com.example.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.*;

@Table(name = "project")
@Entity
@Data
@NoArgsConstructor
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long Id;
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


    @OneToMany(
            cascade = CascadeType.ALL,
            //Numele field-ului din clasa TASK care are referinta catre obiectul PROJECT.
            mappedBy = "project",
            orphanRemoval = true
    )
    private List<Task> tasksList = new ArrayList<>();

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
