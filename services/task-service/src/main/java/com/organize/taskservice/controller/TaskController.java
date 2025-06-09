package com.organize.taskservice.controller;

import com.organize.taskservice.exception.NotAdminException;
import com.organize.taskservice.exception.TaskException;
import com.organize.taskservice.exception.TaskNotFoundException;
import com.organize.taskservice.model.Task;
import com.organize.taskservice.model.TaskStatus;
import com.organize.taskservice.dto.UserDto;
import com.organize.taskservice.service.TaskService;
import com.organize.taskservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;
    private final UserService userService;

    /**
     * Creates a new task. Only an admin user can create a task.
     */
    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Task task, @RequestHeader("Authorization") String jwt) 
            throws NotAdminException {

        UserDto user = userService.getUserProfile(jwt);
        Task createdTask = taskService.createTask(task, user.getRole());
        return new ResponseEntity<>(createdTask, HttpStatus.CREATED);
    }

    /**
     * Retrieves a task by its ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Integer id, @RequestHeader("Authorization") String jwt) 
            throws NotAdminException, TaskNotFoundException {

        UserDto user = userService.getUserProfile(jwt);
        Task task = taskService.getTaskById(id);
        return new ResponseEntity<>(task, HttpStatus.OK);
    }

    /**
     * Retrieves tasks assigned to the user, optionally filtered by status.
     */
    @GetMapping("/user")
    public ResponseEntity<List<Task>> getAssignedUsersTask(@RequestParam(required = false) TaskStatus status, 
                                                           @RequestHeader("Authorization") String jwt) throws Exception {

        UserDto user = userService.getUserProfile(jwt);
        List<Task> tasks = taskService.assignedUsersTask(user.getId(), status);
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    /**
     * Retrieves all tasks, optionally filtered by status.
     */
    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks(@RequestParam(required = false) TaskStatus status, 
                                                  @RequestHeader("Authorization") String jwt) throws Exception {

        UserDto user = userService.getUserProfile(jwt);
        List<Task> tasks = taskService.getAllTasks(status);
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    /**
     * Assigns a task to a user. Only an admin user can assign tasks.
     */
    @PutMapping("/{id}/user/{userId}/assigned")
    public ResponseEntity<Task> assignTaskToUser(@PathVariable Integer id, @PathVariable Integer userId, 
                                                 @RequestHeader("Authorization") String jwt) 
            throws TaskException, TaskNotFoundException {

        UserDto user = userService.getUserProfile(jwt);
        Task task = taskService.assignedToUser(userId, id);
        return new ResponseEntity<>(task, HttpStatus.OK);
    }

    /**
     * Updates an existing task.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Integer id, @RequestBody Task task, 
                                           @RequestHeader("Authorization") String jwt) 
            throws TaskException, TaskNotFoundException {

        UserDto user = userService.getUserProfile(jwt);
        Task updatedTask = taskService.updateTask(id, task, user.getId());
        return new ResponseEntity<>(updatedTask, HttpStatus.OK);
    }

    /**
     * Marks a task as completed.
     */
    @PutMapping("/{id}/complete")
    public ResponseEntity<Task> completeTask(@PathVariable Integer id, @RequestHeader("Authorization") String jwt) 
            throws TaskException, TaskNotFoundException {

        UserDto user = userService.getUserProfile(jwt);
        Task task = taskService.completeTask(id);
        return new ResponseEntity<>(task, HttpStatus.OK);
    }

    /**
     * Deletes a task by its ID.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable Integer id, @RequestHeader("Authorization") String jwt) 
            throws TaskException, TaskNotFoundException {

        UserDto user = userService.getUserProfile(jwt);
        taskService.deleteTask(id);
        return new ResponseEntity<>("Task with id " + id + " deleted Successfully", HttpStatus.OK);
    }
}