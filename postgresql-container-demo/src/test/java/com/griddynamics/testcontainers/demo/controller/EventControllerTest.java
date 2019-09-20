package com.griddynamics.testcontainers.demo.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.griddynamics.testcontainers.demo.repository.EventRepository;
import com.griddynamics.testcontainers.demo.model.Event;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.CollectionUtils;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static com.griddynamics.testcontainers.demo.utils.EventUtilsTest.createEvents;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(initializers = {EventControllerTest.PropertiesInitializer.class})
@ActiveProfiles("test")
@Testcontainers
class EventControllerTest {

    @Container
    private static final PostgreSQLContainer POSTGRESQL = new PostgreSQLContainer("postgres:latest");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EventRepository eventRepository;

    @BeforeEach
    void setUp() {
        eventRepository.saveAll(createEvents());
    }

    @Test
    @SneakyThrows
    void testFindUsersBySpecificRoleIfSuccessful() {
        String contentAsString = mockMvc.perform(get("/events")
                .param("eventName", "JClub_GD_Testcontainers")
                .param("city", "Lviv"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<Event> events = objectMapper.readValue(contentAsString, new TypeReference<List<Event>>() {});

        assertFalse(CollectionUtils.isEmpty(events));
        assertEquals(1, events.size());
        assertEquals("JClub_GD_Testcontainers", events.get(0).getName());
        assertEquals("Ukraine", events.get(0).getLocation().getCountry());
        assertEquals("Lviv", events.get(0).getLocation().getCity());
    }

    @Test
    @SneakyThrows
    void testFindUsersBySpecificRoleIfNotFound() {
        String contentAsString = mockMvc.perform(get("/events")
                .param("eventName", "Movie")
                .param("city", "Saratov"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<Event> events = objectMapper.readValue(contentAsString, new TypeReference<List<Event>>() {});

        assertTrue(CollectionUtils.isEmpty(events));
    }

    @AfterEach
    void tearDown() {
        eventRepository.deleteAll();
    }

    static class PropertiesInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues values = TestPropertyValues.of(
                    String.format("spring.datasource.url=%s", POSTGRESQL.getJdbcUrl()));
            values.applyTo(configurableApplicationContext);
        }
    }
}
