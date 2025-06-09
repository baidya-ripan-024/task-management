package com.organize.submissionservice.controller;

import com.organize.submissionservice.exception.NotAdminException;
import com.organize.submissionservice.exception.TaskNotFoundException;
import com.organize.submissionservice.model.Submission;
import com.organize.submissionservice.model.UserDto;
import com.organize.submissionservice.service.SubmissionService;
import com.organize.submissionservice.service.TaskService;
import com.organize.submissionservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/submissions")
public class SubmissionController {

    private final SubmissionService submissionService;
    private final TaskService taskService;
    private final UserService userService;

    /**
     * Endpoint to submit a new task.
     */
    @PostMapping
    public ResponseEntity<Submission> submitTask(@RequestParam Integer taskId,
                                                 @RequestParam String githubLink,
                                                 @RequestHeader("Authorization") String jwt) 
                                                 throws TaskNotFoundException, NotAdminException {
        UserDto user = userService.getUserProfile(jwt);
        Submission submission = submissionService.submitTask(taskId, githubLink, user.getId(), jwt);
        return new ResponseEntity<>(submission, HttpStatus.CREATED);
    }

    /**
     * Endpoint to retrieve a submission by its ID.
     */
    @GetMapping("{id}")
    public ResponseEntity<Submission> getSubmissionById(@PathVariable Integer id,
                                                        @RequestHeader("Authorization") String jwt) throws Exception {
        UserDto user = userService.getUserProfile(jwt);
        Submission submission = submissionService.getTaskSubmissionById(id);
        return new ResponseEntity<>(submission, HttpStatus.OK);
    }

    /**
     * Endpoint to retrieve all submissions for a specific task.
     */
    @GetMapping("/task/{taskId}")
    public ResponseEntity<List<Submission>> getAllTaskSubmissionsByTaskId(@PathVariable Integer taskId,
                                                                          @RequestHeader("Authorization") String jwt) throws Exception {
        UserDto user = userService.getUserProfile(jwt);
        List<Submission> submissions = submissionService.getTaskSubmissionsByTaskId(taskId);
        return new ResponseEntity<>(submissions, HttpStatus.OK);
    }

    /**
     * Endpoint to retrieve all submissions.
     */
    @GetMapping
    public ResponseEntity<List<Submission>> getAllSubmission(@RequestHeader("Authorization") String jwt) throws Exception {
        UserDto user = userService.getUserProfile(jwt);
        List<Submission> submissions = submissionService.getAllSubmission();
        return new ResponseEntity<>(submissions, HttpStatus.OK);
    }

    /**
     * Endpoint to accept or decline a submission.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Submission> acceptOrDeclinedSubmission(@PathVariable Integer id,
                                                                 @RequestParam String status,
                                                                 @RequestHeader("Authorization") String jwt) throws Exception {
        UserDto user = userService.getUserProfile(jwt);
        Submission submission = submissionService.acceptOrDeclinedSubmission(id, status);
        return new ResponseEntity<>(submission, HttpStatus.OK);
    }

    /**
     * Endpoint to delete a submission.
     */
    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteTaskSubmission(@PathVariable Integer id,
                                                       @RequestHeader("Authorization") String jwt) {
        UserDto user = userService.getUserProfile(jwt);
        submissionService.deleteTaskSubmission(id, user.getId());
        return new ResponseEntity<>("Task deleted Successfully", HttpStatus.NO_CONTENT);
    }

    /**
     * Endpoint to update the GitHub link of a submission.
     */
    @PutMapping("{id}/update-link")
    public ResponseEntity<Submission> updateTaskSubmission(@PathVariable Integer id,
                                                           @RequestParam String githubLink,
                                                           @RequestHeader("Authorization") String jwt) {
        UserDto user = userService.getUserProfile(jwt);
        Submission submission = submissionService.updateTaskSubmission(id, githubLink, user.getId());
        return new ResponseEntity<>(submission, HttpStatus.OK);
    }
}