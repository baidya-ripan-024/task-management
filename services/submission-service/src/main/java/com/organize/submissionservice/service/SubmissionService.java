package com.organize.submissionservice.service;

import com.organize.submissionservice.exception.NotAdminException;
import com.organize.submissionservice.exception.TaskNotFoundException;
import com.organize.submissionservice.exception.TaskSubmissionException;
import com.organize.submissionservice.model.Submission;

import java.util.List;

public interface SubmissionService {

    /**
     * Submits a task for a given user.
     */
    Submission submitTask(Integer taskId, String githubLink, Integer userId, String jwt)
            throws TaskSubmissionException, TaskNotFoundException, NotAdminException;

    /**
     * Retrieves a task submission by its ID.
     */
    Submission getTaskSubmissionById(Integer submissionId) throws TaskSubmissionException;

    /**
     * Retrieves all submissions.
     */
    List<Submission> getAllSubmission();

    /**
     * Retrieves submissions for a specific task.
     */
    List<Submission> getTaskSubmissionsByTaskId(Integer taskId) throws TaskSubmissionException;

    /**
     * Accepts or declines a task submission.
     */
    Submission acceptOrDeclinedSubmission(Integer submissionId, String status) throws TaskNotFoundException;

    /**
     * Deletes a task submission.
     */
    void deleteTaskSubmission(Integer submissionId, Integer userId) throws TaskSubmissionException;

    /**
     * Updates the GitHub link of a task submission.
     */
    Submission updateTaskSubmission(Integer submissionId, String githubLink, Integer userId)
            throws TaskSubmissionException;
}