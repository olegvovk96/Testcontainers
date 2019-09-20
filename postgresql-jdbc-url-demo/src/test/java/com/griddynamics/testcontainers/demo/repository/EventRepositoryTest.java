package com.griddynamics.testcontainers.demo.repository;

import com.griddynamics.testcontainers.demo.model.Event;
import com.griddynamics.testcontainers.demo.utils.EventUtilsTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.CollectionUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
@ActiveProfiles("test")
class EventRepositoryTest {

    @Autowired
    private EventRepository eventRepository;

    @BeforeEach
    void setUp() {
        eventRepository.saveAll(EventUtilsTest.createEvents());
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
}
