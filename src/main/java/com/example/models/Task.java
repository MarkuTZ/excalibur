package com.example.models;

import com.example.models.enums.Priority;
import com.example.models.enums.Status;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Table(name = "task")
@Entity
@Getter
@Setter
@NoArgsConstructor
public class Task {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column
	private String name;

	@Column
	private String description;

	@Enumerated(EnumType.STRING)
	@Column(columnDefinition = "ENUM('LOW', 'MEDIUM', 'HIGH')")
	private Priority priority;

	@Column
	private Date createDate;

	@Column
	@JsonFormat(pattern = "yyyy/MM/dd")
	private Date startDate;

	@Column
	@JsonFormat(pattern = "yyyy/MM/dd")
	private Date endDate;

	@Enumerated(EnumType.STRING)
	@Column(columnDefinition = "ENUM('TODO', 'IN_PROGRESS', 'IN_REVIEW', 'DONE')")
	private Status status;

	@ManyToOne
	private User creator;

	@ManyToOne
	private User assignee;

	@JsonBackReference
	@ManyToOne
	@JoinColumn(name = "project_id")
	private Project project;

}
