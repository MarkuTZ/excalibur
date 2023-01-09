package com.example.dto;

import com.example.models.User;
import com.example.models.enums.Status;
import lombok.Data;

import java.util.Date;

@Data
public class ProjectDto {

	private long id;

	private String name;

	private String description;

	private Date createDate;

	private Date deadline;

	private User owner;

	private Status projectStatus;

}
