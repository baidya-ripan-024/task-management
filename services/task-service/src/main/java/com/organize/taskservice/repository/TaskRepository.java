package com.organize.taskservice.repository;

import com.organize.taskservice.model.Task;
import com.organize.taskservice.model.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {

    /**
     * Finds all tasks assigned to the given user id.
     */
    List<Task> findByAssignedUserId(Integer userId);

    // Fetch tasks assigned to a specific user with specific status
    List<Task> findByAssignedUserIdAndTaskStatus(Integer userId, TaskStatus status);
}