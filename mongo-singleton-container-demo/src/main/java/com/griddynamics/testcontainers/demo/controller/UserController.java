package com.griddynamics.testcontainers.demo.controller;

import com.griddynamics.testcontainers.demo.model.User;
import com.griddynamics.testcontainers.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    public User findUserById(@PathVariable String id) {
        return userService.findUserById(id);
    }

    @GetMapping
    public List<User> findUsersByRole(@RequestParam String role) {
        return userService.findUsersByRole(role);
    }
}
