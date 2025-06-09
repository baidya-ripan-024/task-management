package com.organize.taskservice.service;

import com.organize.taskservice.exception.NotAdminException;
import com.organize.taskservice.exception.TaskException;
import com.organize.taskservice.exception.TaskNotFoundException;
import com.organize.taskservice.model.Task;
import com.organize.taskservice.model.TaskStatus;
import com.organize.taskservice.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class provides the implementation of the TaskService interface.
 * It is responsible for managing tasks, such as creating, updating, and retrieving tasks.
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    /**
     * Creates a new task. Only an admin user can create a task.
     */
    public Task createTask(Task task, String requesterRole) throws TaskException, NotAdminException {
        // Validate if the requester has admin privileges
        if (!"ROLE_ADMIN".equals(requesterRole)) {
            throw new NotAdminException("Access denied: User is not an admin.");
        }

        // Validate task details (e.g., title and description should not be empty)
        if (task.getTitle() == null || task.getTitle().isEmpty()) {
            throw new TaskException("Task title cannot be empty.");
        }
        if (task.getDescription() == null || task.getDescription().isEmpty()) {
            throw new TaskException("Task description cannot be empty.");
        }

        // Create a new task object and set default values
        Task createdTask = new Task();

        createdTask.setTitle(task.getTitle());
        createdTask.setDescription(task.getDescription());
        createdTask.setDeadline(task.getDeadline());
        createdTask.setImage(task.getImage());
        createdTask.setTechStacks(task.getTechStacks());

        createdTask.setAssignedUserId(null); // Initially, the task is not assigned to anyone

        // Initially, we never assigned any task to any user, and if, but still, status should not be anything
        // but, it would be PENDING
        createdTask.setTaskStatus(TaskStatus.PENDING); // Default status is PENDING,
        createdTask.setCreatedAt(LocalDateTime.now());

        // Save the task in the database
        return taskRepository.save(createdTask);
    }

    /**
     * Retrieves a task by its ID.
     *
     * @param taskId the ID of the task to be retrieved
     * @return the retrieved task
     * @throws TaskNotFoundException if the task is not found
     */
    public Task getTaskById(Integer taskId) throws TaskNotFoundException {
        return taskRepository.findById(taskId).orElseThrow(() -> new TaskNotFoundException("Task not found with id" + taskId));
    }

    /**
     * Retrieves all tasks, optionally filtered by their status.
     */
    public List<Task> getAllTasks(TaskStatus status) {

        List<Task> tasks = taskRepository.findAll(); // get the all tasks
        if (status == null) {
            return tasks;
        }

        return tasks.stream()
                .filter(task -> task.getTaskStatus() != null && task.getTaskStatus() == status)
                .collect(Collectors.toList());
    }

    /**
     * Updates an existing task with new details.
     */
    public Task updateTask(Integer taskId, Task task, Integer userId) throws TaskException, TaskNotFoundException {
        Task existingTask = getTaskById(taskId); // get the existing task by its id

        if (existingTask.getTitle() != null) {
            existingTask.setTitle(task.getTitle());
        }
        if (existingTask.getDescription() != null) {
            existingTask.setDescription(task.getDescription());
        }
        if (existingTask.getImage() != null) {
            existingTask.setImage(task.getImage());
        }
        if (existingTask.getDeadline() != null) {
            existingTask.setDeadline(task.getDeadline());
        }
        if (existingTask.getTaskStatus() != null) {
            existingTask.setTaskStatus(task.getTaskStatus());
        }

        return taskRepository.save(existingTask);
    }

    /**
     * Deletes a task by its ID.
     */
    public void deleteTask(Integer taskId) throws TaskException, TaskNotFoundException {
        Task existingTask = getTaskById(taskId);
        taskRepository.delete(existingTask);
    }

    /**
     * Assigns a task to a user.
     */
    public Task assignedToUser(Integer userId, Integer taskId) throws TaskException, TaskNotFoundException {
        Task task = getTaskById(taskId);
        task.setAssignedUserId(userId);
        task.setTaskStatus(TaskStatus.ASSIGNED);

        return taskRepository.save(task);
    }

    /**
     * Retrieves tasks assigned to a user, optionally filtered by status.
     */
    public List<Task> assignedUsersTask(Integer userId, TaskStatus status) throws Exception {
        log.info("Fetching tasks assigned to userId: {} with status: {}", userId, status);

        List<Task> tasks;

        if (status == null) {
            // Fetch all tasks assigned to the user
            tasks = taskRepository.findByAssignedUserId(userId);
        } else {
            // Fetch tasks assigned to the user with the given status
            tasks = taskRepository.findByAssignedUserIdAndTaskStatus(userId, status);
        }

        return tasks;
    }


    /**
     * Marks a task as completed.
     */
    public Task completeTask(Integer taskId) throws TaskException, TaskNotFoundException {
        Task task = getTaskById(taskId);
        task.setTaskStatus(TaskStatus.COMPLETED);
        return taskRepository.save(task);
    }
}