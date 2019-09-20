package com.griddynamics.testcontainers.demo.repository;

import com.griddynamics.testcontainers.demo.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface UserRepository extends MongoRepository<User, String> {

    @Query("{'roles':{$regex:?0}}")
    List<User> findUsersWithSpecificRole(String role);
}
