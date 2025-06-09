package com.organize.userservice.controller;

import com.organize.userservice.config.JwtProvider;
import com.organize.userservice.exception.UserException;
import com.organize.userservice.model.User;
import com.organize.userservice.repository.UserRepository;
import com.organize.userservice.request.LoginRequest;
import com.organize.userservice.request.RegistrationRequest;
import com.organize.userservice.response.LoginResponse;
import com.organize.userservice.response.RegisterResponse;
import com.organize.userservice.service.impl.CustomUserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * This class contains the REST endpoints for user authentication.
 * It contains endpoints for registering a new user and logging in an existing user.
 * The endpoints are protected by Spring Security.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthenticationController {
    private static final Logger log = LoggerFactory.getLogger(AuthenticationController.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CustomUserDetailsServiceImpl customUserDetailsService;

    /**
     * This endpoint is used to register a new user.
     * It takes a RegistrationRequest object as input and creates a new user in the database.
     * It then generates a JWT token for the user and returns it in the response.
     * If the user already exists, it throws a UserException.
     * @param user The user details to register.
     * @return A RegisterResponse object containing the JWT token and a success message.
     * @throws UserException If the user already exists.
     */
    @PostMapping("/signup")
    public ResponseEntity<RegisterResponse> registerUser(@RequestBody RegistrationRequest user) throws UserException {
        log.info("Registering user with email {}", user.getEmail());
        String email = user.getEmail();
        String password = user.getPassword();
        String name = user.getName();
        String role = user.getRole();

        User isUserExists = userRepository.findByEmail(email);
        if(isUserExists != null) throw new UserException("User already exists");

        // Create a new User
        User createdUser = new User();
        createdUser.setName(name);
        createdUser.setEmail(email);
        createdUser.setPassword(passwordEncoder.encode(password)); // Make Sure, encode the password before save it to DB
        createdUser.setRole(role);

        User savedUser = userRepository.save(createdUser); // save the user

        log.info("User registered successfully. Generating JWT token for user {}", email);
        Authentication authentication = new UsernamePasswordAuthenticationToken(email, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = JwtProvider.generateToken(authentication); // generate the token

        var response = new RegisterResponse(token, "User Registered Successfully!");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * This endpoint is used to log in an existing user.
     * It takes an Login Request object as input and authenticates the user in the database.
     * It then generates a JWT token for the user and returns it in the response.
     * If the user is not found or the password is invalid, it throws a BadCredentialsException.
     * @param loginRequest The user login details.
     * @return A LoginResponse object containing the JWT token and a success message.
     * @throws BadCredentialsException If the user is not found or the password is invalid.
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(@RequestBody LoginRequest loginRequest){
        log.info("Logging in user with email [{}]", loginRequest.getEmail());
        String username = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        Authentication authentication = authenticate(username, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        log.info("User logged in successfully. Generating JWT token for user {}", username);
        String token = JwtProvider.generateToken(authentication);

        var response = new LoginResponse(token, "Login Successful!");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * This method is used to authenticate a user.
     * It takes a username and password as input and checks if the user exists in the database.
     * If the user exists, it checks if the password is valid.
     * If the password is valid, it returns an Authentication object.
     * If the user is not found or the password is invalid, it throws a BadCredentialsException.
     * @param username The username to authenticate.
     * @param password The password to check.
     * @return An Authentication object containing the user details.
     * @throws BadCredentialsException If the user is not found or the password is invalid.
     */
    private Authentication authenticate(String username, String password) {
        log.info("Authenticating user with email {}", username);
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

        if(userDetails == null){
            log.error("User not found!");
            throw new BadCredentialsException("Invalid Credentials!");
        }
        if(! passwordEncoder.matches(password, userDetails.getPassword())){
            log.error("Invalid Password!");
            throw new BadCredentialsException("Invalid Password!");
        }
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}