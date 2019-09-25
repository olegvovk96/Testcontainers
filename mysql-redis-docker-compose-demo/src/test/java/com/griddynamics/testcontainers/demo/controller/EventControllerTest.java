package com.griddynamics.testcontainers.demo.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.griddynamics.testcontainers.demo.model.Event;
import com.griddynamics.testcontainers.demo.repository.EventRepository;
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
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.File;
import java.util.List;

import static com.griddynamics.testcontainers.demo.utils.EventUtilsTest.createEvents;
import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@ContextConfiguration(initializers = {EventControllerTest.PropertiesInitializer.class})
@Testcontainers
class EventControllerTest {

    private static final String MYSQL_SERVICE_NAME = "mysql";

    private static final Integer MYSQL_SERVICE_PORT = 3306;

    private static final String REDIS_SERVICE_NAME = "redis-cache";

    private static final Integer REDIS_SERVICE_PORT = 6379;

    @Container
    private static final DockerComposeContainer COMPOSE_CONTAINER = new DockerComposeContainer(
            new File("src/test/resources/docker-compose.yml"))
            .withExposedService(MYSQL_SERVICE_NAME, MYSQL_SERVICE_PORT)
            .withExposedService(REDIS_SERVICE_NAME, REDIS_SERVICE_PORT);

    private static final String MYSQL_DATA_SOURCE_PROPERTY_FORMAT = "spring.datasource.url=jdbc:mysql://%s:%s/%s";

    private static final String DATABASE_NAME = "test?createDatabaseIfNotExist=true&serverTimezone=UTC";

    private static final String REDIS_HOST_PROPERTY_FORMAT = "spring.redis.host=%s";

    private static final String REDIS_PORT_PROPERTY_FORMAT = "spring.redis.port=%s";

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
    void testFindEventsByEventNameAndCityIfSuccessful() {
        objectMapper.readValue(getEventResponseBody("JClub_GD_Testcontainers", "Lviv"), new TypeReference<List<Event>>() {});

        eventRepository.deleteAll();

        List<Event> cachedEvents = objectMapper.readValue(
                getEventResponseBody("JClub_GD_Testcontainers", "Lviv"), new TypeReference<List<Event>>() {});

        assertFalse(CollectionUtils.isEmpty(cachedEvents));
        assertEquals(1, cachedEvents.size());
        assertEquals("JClub_GD_Testcontainers", cachedEvents.get(0).getName());
        assertEquals("Ukraine", cachedEvents.get(0).getLocation().getCountry());
        assertEquals("Lviv", cachedEvents.get(0).getLocation().getCity());
    }

    @Test
    @SneakyThrows
    void testFindEventsByEventNameAndCityIfNotFound() {
        List<Event> events = objectMapper.readValue(
                getEventResponseBody("Movie", "Saratov"), new TypeReference<List<Event>>() {});

        assertTrue(CollectionUtils.isEmpty(events));
    }

    @SneakyThrows
    private String getEventResponseBody(String eventName, String city) {
        return mockMvc.perform(get("/events")
                .param("eventName", eventName)
                .param("city", city))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    @AfterEach
    void tearDown() {
        eventRepository.deleteAll();
    }

    static class PropertiesInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues values = TestPropertyValues.of(
                    format(
                            MYSQL_DATA_SOURCE_PROPERTY_FORMAT,
                            COMPOSE_CONTAINER.getServiceHost(MYSQL_SERVICE_NAME, MYSQL_SERVICE_PORT),
                            COMPOSE_CONTAINER.getServicePort(MYSQL_SERVICE_NAME, MYSQL_SERVICE_PORT),
                            DATABASE_NAME),
                    format(REDIS_HOST_PROPERTY_FORMAT, COMPOSE_CONTAINER.getServiceHost(REDIS_SERVICE_NAME, REDIS_SERVICE_PORT)),
                    format(REDIS_PORT_PROPERTY_FORMAT, COMPOSE_CONTAINER.getServicePort(REDIS_SERVICE_NAME, REDIS_SERVICE_PORT)));
            values.applyTo(configurableApplicationContext);
        }
    }
}
