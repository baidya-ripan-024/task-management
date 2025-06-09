package com.organize.taskservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class represents a User in the system.
 * It contains the user's id, name, email, password and role.
 * The role can be either "USER" or "ADMIN".
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Integer id;

    private String name;
    private String email;
    private String password;
    private String role;
}