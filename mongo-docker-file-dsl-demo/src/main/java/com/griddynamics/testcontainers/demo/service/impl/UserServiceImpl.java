package com.griddynamics.testcontainers.demo.service.impl;

import com.griddynamics.testcontainers.demo.exception.UserNotFoundException;
import com.griddynamics.testcontainers.demo.service.UserService;
import com.griddynamics.testcontainers.demo.exception.UserWithRoleNotExistException;
import com.griddynamics.testcontainers.demo.model.User;
import com.griddynamics.testcontainers.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User findUserById(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    @Override
    public List<User> findUsersByRole(String role) {
        List<User> users = userRepository.findUsersWithSpecificRole(role);

        if (CollectionUtils.isEmpty(users)) {
            throw new UserWithRoleNotExistException(role);
        } else {
            return users;
        }
    }
}
