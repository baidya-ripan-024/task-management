package com.organize.submissionservice.service;

import com.organize.submissionservice.exception.NotAdminException;
import com.organize.submissionservice.exception.TaskException;
import com.organize.submissionservice.exception.TaskNotFoundException;
import com.organize.submissionservice.model.TaskDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "TASK-SERVICE", url = "http://localhost:8002")
public interface TaskService {

    /**
     * Retrieves a task by its ID.
     */
    @GetMapping("/api/tasks/{id}")
    TaskDto getTaskById(@PathVariable Integer id,
                        @RequestHeader("Authorization") String jwt) throws NotAdminException, TaskNotFoundException;

    /**
     * Marks a task as completed.
     */
    @PutMapping("/api/tasks/{id}/complete")
    TaskDto completeTask(@PathVariable Integer id) throws TaskException, TaskNotFoundException;
}