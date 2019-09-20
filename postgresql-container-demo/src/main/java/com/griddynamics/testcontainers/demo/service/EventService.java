package com.griddynamics.testcontainers.demo.service;

import com.griddynamics.testcontainers.demo.model.Event;

import java.util.List;

public interface EventService {

    List<Event> findEventsByEventNameAndCity(String eventName, String city);
}
