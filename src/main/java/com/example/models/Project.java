package com.example.models;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Table(name = "project")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long Id;
    @Column
    private String name;
    @Column
    private String description;
    @Column
    private Date create_date;
    @Column
    private Date deadline;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private User owner;

    @OneToMany(cascade= {CascadeType.ALL})
    @JoinColumn(name="project_id")
            private List<Task> taskList;


}
