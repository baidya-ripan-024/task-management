package com.organize.userservice.controller;

import com.organize.userservice.model.User;
import com.organize.userservice.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller for managing user-related operations.
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Retrieves the profile of the user based on the provided JWT.
     * 
     * @param jwt JWT token for authorization.
     * @return ResponseEntity containing the user profile.
     */
    @GetMapping("/profile")
    public ResponseEntity<User> getUserByJwt(@RequestHeader("Authorization") String jwt) {
        log.info("Getting user details via jwt {}", jwt);
        User user = userService.findUserByJwt(jwt);
        log.info("User details retrieved successfully for jwt {}", jwt);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    /**
     * Retrieves all users' details.
     * 
     * @param jwt JWT token for authorization.
     * @return ResponseEntity containing the list of users.
     */
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers(@RequestHeader("Authorization") String jwt) {
        log.info("Fetching all users");
        List<User> users = userService.getAllUsers();
        log.info("All users retrieved successfully");
        return new ResponseEntity<>(users, HttpStatus.OK);
    }
}