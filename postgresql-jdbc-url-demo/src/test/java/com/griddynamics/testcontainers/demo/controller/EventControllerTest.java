package com.griddynamics.testcontainers.demo.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.griddynamics.testcontainers.demo.model.Event;
import com.griddynamics.testcontainers.demo.utils.EventUtilsTest;
import com.griddynamics.testcontainers.demo.repository.EventRepository;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.CollectionUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class EventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EventRepository eventRepository;

    @BeforeEach
    void setUp() {
        eventRepository.saveAll(EventUtilsTest.createEvents());
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
}
