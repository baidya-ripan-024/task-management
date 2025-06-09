package com.organize.submissionservice.service;

import com.organize.submissionservice.model.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

// Give the name of the service and the url on which the service has run
// This feigned client will be used to call the user-service to get the user profile
@FeignClient(name = "USER-SERVICE", url = "http://localhost:8001")
public interface UserService {

    // This method will be used to get the user profile based on the jwt token
    @GetMapping("/api/users/profile")
    public UserDto getUserProfile(@RequestHeader("Authorization") String jwt);
}