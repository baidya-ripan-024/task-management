package com.organize.taskservice.service;

import com.organize.taskservice.exception.NotAdminException;
import com.organize.taskservice.exception.TaskException;
import com.organize.taskservice.exception.TaskNotFoundException;
import com.organize.taskservice.model.Task;
import com.organize.taskservice.model.TaskStatus;

import java.util.List;

public interface TaskService {

    /**
     * Creates a new task. Only an admin user can create a task.
     */
    Task createTask(Task task, String requesterRole) throws TaskException, NotAdminException;

    /**
     * Retrieves a task by its ID.
     */
    Task getTaskById(Integer taskId) throws TaskNotFoundException;

    /**
     * Retrieves all tasks, optionally filtered by their status.
     */
    List<Task> getAllTasks(TaskStatus status);

    /**
     * Updates an existing task with new details.
     */
    Task updateTask(Integer taskId, Task task, Integer userId) throws TaskException, TaskNotFoundException;

    /**
     * Deletes a task by its ID.
     */
    void deleteTask(Integer taskId) throws TaskException, TaskNotFoundException;

    /**
     * Assigns a task to a user.
     */
    Task assignedToUser(Integer userId, Integer taskId) throws TaskException, TaskNotFoundException;

    /**
     * Retrieves tasks assigned to a user, optionally filtered by status.
     */
    List<Task> assignedUsersTask(Integer userId, TaskStatus status) throws Exception;

    /**
     * Marks a task as completed.
     */
    Task completeTask(Integer taskId) throws TaskException, TaskNotFoundException;
}