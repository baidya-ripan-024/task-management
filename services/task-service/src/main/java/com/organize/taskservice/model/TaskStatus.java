package com.organize.taskservice.model;

/**
 * Enum to represent the status of a task.
 * A task can have one of the following statuses:
 * <ul>
 *     <li>PENDING: The task is pending and has not been assigned to anyone.</li>
 *     <li>ASSIGNED: The task has been assigned to someone.</li>
 *     <li>COMPLETED: The task has been completed.</li>
 * </ul>
 */
public enum TaskStatus {
    PENDING("PENDING"),
    ASSIGNED("ASSIGNED"),
    COMPLETED("COMPLETED");

    private final String task;

    TaskStatus(String task) {
        this.task = task;
    }

    public String getTask() {
        return task;
    }
}