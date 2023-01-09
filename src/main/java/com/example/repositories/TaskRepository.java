package com.example.repositories;

import com.example.models.Task;
import com.example.models.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

	List<Task> findAllByProject_Id(long project_id);

	List<Task> findAllByStatusAndProject_Id(Status status, long project_id);

}
