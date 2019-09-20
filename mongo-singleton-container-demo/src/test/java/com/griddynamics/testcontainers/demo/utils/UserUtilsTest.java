package com.griddynamics.testcontainers.demo.utils;

import com.griddynamics.testcontainers.demo.model.User;

import java.util.Arrays;
import java.util.List;

import static java.util.Collections.singletonList;

public class UserUtilsTest {

    private UserUtilsTest() {
        throw new IllegalStateException("Utility class");
    }

    public static List<User> createUsers() {
        User user1 = User.of()
                .id("1")
                .firstName("Ivan")
                .lastName("Sydorov")
                .age(23)
                .roles(Arrays.asList("Developer", "Admin"))
                .build();

        User user2 = User.of()
                .id("2")
                .firstName("Petro")
                .lastName("Ivanov")
                .age(32)
                .roles(singletonList("User"))
                .build();

        User user3 = User.of()
                .id("3")
                .firstName("Nazar")
                .lastName("Burak")
                .age(21)
                .roles(singletonList("Developer"))
                .build();

        return Arrays.asList(user1, user2, user3);
    }
}
