package com.organize.submissionservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskDto {
    private Integer id; // Unique identifier for each task

    private String title; // Title of the task
    private String description; // Detailed description of the task
    private String image; // Image associated with the task
    private Integer assignedUserId; // ID of the user to whom the task is assigned

    private List<String> techStacks = new ArrayList<>(); // List of technology stacks used for the task

    private TaskStatus taskStatus; // Status of the task, e.g., PENDING, ASSIGNED, COMPLETED
    private LocalDateTime deadline; // Deadline for the task completion
    private LocalDateTime createdAt; // Timestamp when the task was created
}