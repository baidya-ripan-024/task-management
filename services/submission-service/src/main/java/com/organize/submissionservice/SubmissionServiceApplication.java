package com.organize.submissionservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class SubmissionServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SubmissionServiceApplication.class, args);
	}

}
