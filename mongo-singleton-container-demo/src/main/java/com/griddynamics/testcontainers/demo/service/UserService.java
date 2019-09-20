package com.griddynamics.testcontainers.demo.service;

import com.griddynamics.testcontainers.demo.model.User;

import java.util.List;

public interface UserService {

    User findUserById(String id);

    List<User> findUsersByRole(String role);
}
