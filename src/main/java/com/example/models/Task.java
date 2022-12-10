package com.example.models;

import com.example.models.enums.Priority;
import com.example.models.enums.Status;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "task")
@Data
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long Id;
    @Column
    private String name;
    @Column
    private String description;
    @Column
    private Priority priority;
    @Column
    private Date create_date;
    @Column
    private Date start_date;
    @Column
    private Date end_date;
    @Column
    private Status status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id")
    private User creator;

    @ManyToMany(mappedBy = "tasks")
    private Set<User> assignedUsers = new HashSet<>();






}
