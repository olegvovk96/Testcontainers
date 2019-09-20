package com.griddynamics.testcontainers.demo.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.griddynamics.testcontainers.demo.model.User;
import com.griddynamics.testcontainers.demo.repository.UserRepository;
import com.griddynamics.testcontainers.demo.utils.UserUtilsTest;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.CollectionUtils;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.images.builder.ImageFromDockerfile;
import org.testcontainers.images.builder.dockerfile.DockerfileBuilder;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(initializers = {UserControllerTest.PropertiesInitializer.class})
@Testcontainers
class UserControllerTest {

    @Container
    private static final GenericContainer MONGO_CONTAINER = new GenericContainer(new ImageFromDockerfile()
            .withDockerfileFromBuilder(UserControllerTest::buildDockerFile))
            .withExposedPorts(27017);

    private static final String DOCKER_IMAGE_NAME = "mongo:latest";

    private static final String MONGODB_URI_PROPERTY_FORMAT = "spring.data.mongodb.uri=mongodb://%s:%s/test";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.saveAll(UserUtilsTest.createUsers());
    }

    @Test
    @SneakyThrows
    void testFindUserByIdIfSuccessful() {
        String responseBody = mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        User user = objectMapper.readValue(responseBody, User.class);

        assertEquals("Ivan", user.getFirstName());
        assertEquals("Sydorov", user.getLastName());
        assertEquals(23, user.getAge());
        assertEquals(Arrays.asList("Developer", "Admin"), user.getRoles());
    }

    @Test
    @SneakyThrows
    void testFindUserByIdIfNotFound() {
        MvcResult result = mockMvc.perform(get("/users/4"))
                .andExpect(status().is4xxClientError())
                .andReturn();

        assertEquals(result.getResponse().getStatus(), NOT_FOUND.value());
    }

    @Test
    @SneakyThrows
    void testFindUsersBySpecificRoleIfSuccessful() {
        String responseBody = mockMvc.perform(get("/users")
                .param("role", "Developer"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<User> users = objectMapper.readValue(responseBody, new TypeReference<List<User>>() {});

        assertFalse(CollectionUtils.isEmpty(users));
        assertEquals(2, users.size());
        users.forEach(user -> assertTrue(user.getRoles().contains("Developer")));
    }

    @Test
    @SneakyThrows
    void testFindUsersBySpecificRoleIfUsersWithFollowRoleNotExist() {
        MvcResult result = mockMvc.perform(get("/users")
                .param("role", "Viking"))
                .andExpect(status().is4xxClientError())
                .andReturn();

        assertEquals(result.getResponse().getStatus(), BAD_REQUEST.value());
    }

    private static void buildDockerFile(DockerfileBuilder builder) {
        builder.from(DOCKER_IMAGE_NAME).build();
    }

    static class PropertiesInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues values = TestPropertyValues.of(
                    String.format(
                            MONGODB_URI_PROPERTY_FORMAT,
                            MONGO_CONTAINER.getContainerIpAddress(),
                            MONGO_CONTAINER.getMappedPort(27017)));
            values.applyTo(configurableApplicationContext);
        }
    }
}
