package com.griddynamics.testcontainers.demo.controller;

import com.griddynamics.testcontainers.demo.model.Event;
import com.griddynamics.testcontainers.demo.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @GetMapping
    public List<Event> findEventsByNameAndCity(@RequestParam String eventName,
                                               @RequestParam String city) {
        return eventService.findEventsByEventNameAndCity(eventName, city);
    }
}
