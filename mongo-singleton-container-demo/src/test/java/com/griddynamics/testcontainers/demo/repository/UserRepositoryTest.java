package com.griddynamics.testcontainers.demo.repository;

import com.griddynamics.testcontainers.demo.AbstractContainerTest;
import com.griddynamics.testcontainers.demo.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.util.CollectionUtils;

import java.util.List;

import static com.griddynamics.testcontainers.demo.utils.UserUtilsTest.createUsers;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataMongoTest
public class UserRepositoryTest extends AbstractContainerTest {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.saveAll(createUsers());
    }

    @Test
    void testFindUsersWithSpecificRole() {
        List<User> users = userRepository.findUsersWithSpecificRole("Developer");

        assertFalse(CollectionUtils.isEmpty(users));
        assertEquals(2, users.size());
        users.forEach(user -> assertTrue(user.getRoles().contains("Developer")));
    }

    @Test
    void testFindUsersBySpecificRoleIfNotFound() {
        List<User> users = userRepository.findUsersWithSpecificRole("Viking");

        assertTrue(CollectionUtils.isEmpty(users));
    }
}
