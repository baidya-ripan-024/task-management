package com.organize.userservice.service;

import com.organize.userservice.model.User;

import java.util.List;

public interface UserService {

    User findUserByJwt(String jwt);

    List<User> getAllUsers();
}
