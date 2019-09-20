package com.griddynamics.testcontainers.demo.service.impl;

import com.griddynamics.testcontainers.demo.model.Event;
import com.griddynamics.testcontainers.demo.repository.EventRepository;
import com.griddynamics.testcontainers.demo.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;

    @Override
    public List<Event> findEventsByEventNameAndCity(String eventName, String city) {
        return eventRepository.findEventsByEventNameAndCity(eventName, city);
    }
}
