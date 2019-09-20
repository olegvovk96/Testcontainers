package com.griddynamics.testcontainers.demo.utils;

import com.griddynamics.testcontainers.demo.model.Event;
import com.griddynamics.testcontainers.demo.model.Location;

import java.util.Arrays;
import java.util.List;

public class EventUtilsTest {

    private EventUtilsTest() {
        throw new IllegalStateException("Utility class");
    }

    public static List<Event> createEvents() {
        Event event1 = Event.of()
                .name("JClub_GD_Testcontainers")
                .location(Location.of()
                        .country("Ukraine")
                        .city("Lviv")
                        .build())
                .build();

        Event event2 = Event.of()
                .name("Spring Boot")
                .location(Location.of()
                        .country("Ukraine")
                        .city("Kyiv")
                        .build())
                .build();

        Event event3 = Event.of()
                .name("Kubernetes")
                .location(Location.of()
                        .country("Ukraine")
                        .city("Lviv")
                        .build())
                .build();

        return Arrays.asList(event1, event2, event3);
    }
}
