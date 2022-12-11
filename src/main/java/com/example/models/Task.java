package com.example.models;

import com.example.models.enums.Priority;
import com.example.models.enums.Status;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Table(name = "task")
@Entity
@Data
@NoArgsConstructor
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
    private Date createDate;
    @Column
    private Date startDate;
    @Column
    private Date endDate;
    @Column
    private Status status;

    @ManyToOne
    private User creator;

    @ManyToOne
    private User assignee;

    @ManyToOne
    private Project project;
}
