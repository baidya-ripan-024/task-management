package com.organize.submissionservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * This class represents the Submission entity in the database.
 * It contains the submission's id, task id, github link, user id, status and submission time.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Submission {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer submissionId;

    /**
     * Id of the task associated with this submission
     */
    private Integer taskId;

    /**
     * Link to the submission on a platform like GitHub
     */
    private String githubLink;

    /**
     * I'd of the user who submitted the task
     */
    private Integer userId;

    /**
     * The Status of the submission can be PENDING, ACCEPTED or DECLINED
     */
    private String status = "PENDING";

    /**
     * Time when the submission was made
     */
    private LocalDateTime submissionTime;
}