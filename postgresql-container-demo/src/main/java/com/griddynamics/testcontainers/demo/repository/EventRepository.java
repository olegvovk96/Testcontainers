package com.griddynamics.testcontainers.demo.repository;

import com.griddynamics.testcontainers.demo.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Integer> {

    @SuppressWarnings("ALL")
    @Query(
            value = "SELECT * FROM event WHERE event_name = ?1 AND location ->> 'country' = 'Ukraine'" +
                    " AND location ->> 'city' = ?2",
            nativeQuery = true)
    List<Event> findEventsByEventNameAndCity(String eventName, String city);

}
