package com.organize.taskservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents the Task entity in the database.
 * It contains the task's id, title, description, image, assigned user id,
 * tech stacks, task status, deadline, and created at time.
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @NotEmpty(message = "Title cannot be empty")
    private String title;

    @NotEmpty(message = "Description cannot be empty")
    private String description;

    private String image;

    private Integer assignedUserId;

    @Size(min = 1, message = "At least one tech stack is required")
    private List<String> techStacks = new ArrayList<>();


    private TaskStatus taskStatus;

    private LocalDateTime deadline;

    private LocalDateTime createdAt;
}