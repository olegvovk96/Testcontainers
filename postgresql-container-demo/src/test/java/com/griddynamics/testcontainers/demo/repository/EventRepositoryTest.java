package com.griddynamics.testcontainers.demo.repository;

import com.griddynamics.testcontainers.demo.model.Event;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.util.CollectionUtils;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static com.griddynamics.testcontainers.demo.utils.EventUtilsTest.createEvents;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
@ActiveProfiles("test")
@ContextConfiguration(initializers = {EventRepositoryTest.PropertiesInitializer.class})
@Testcontainers
class EventRepositoryTest {

    @Container
    private static final PostgreSQLContainer POSTGRESQL = new PostgreSQLContainer("postgres:latest");

    @Autowired
    private EventRepository eventRepository;

    @BeforeEach
    void setUp() {
        eventRepository.saveAll(createEvents());
    }

    @Test
    void testFindEventsByEventNameAndCity() {
        List<Event> events = eventRepository
                .findEventsByEventNameAndCity("JClub_GD_Testcontainers", "Lviv");

        assertFalse(CollectionUtils.isEmpty(events));
        assertEquals(1, events.size());
        assertEquals("JClub_GD_Testcontainers", events.get(0).getName());
        assertEquals("Ukraine", events.get(0).getLocation().getCountry());
        assertEquals("Lviv", events.get(0).getLocation().getCity());
    }

    @Test
    void testFindEventsByEventNameAndCityIfNotFound() {
        List<Event> events = eventRepository
                .findEventsByEventNameAndCity("Movie", "Saratov");

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
