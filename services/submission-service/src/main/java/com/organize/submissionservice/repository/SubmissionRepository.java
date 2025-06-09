package com.organize.submissionservice.repository;

import com.organize.submissionservice.model.Submission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubmissionRepository extends JpaRepository<Submission, Integer> {

    public List<Submission> findByTaskId(Integer taskId);
}
