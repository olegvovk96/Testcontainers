package com.griddynamics.testcontainers.demo.service.impl;

import com.griddynamics.testcontainers.demo.model.Event;
import com.griddynamics.testcontainers.demo.repository.EventRepository;
import com.griddynamics.testcontainers.demo.service.impl.EventServiceImpl;
import java.util.List;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class EventServiceImplTest {
  @Rule
  public ExpectedException thrown = ExpectedException.none();
  @Test
  public void testfindEventsByEventNameAndCity() throws Exception {
    // Arrange, Act and Assert
    thrown.expect(NullPointerException.class);
    (new EventServiceImpl(null)).findEventsByEventNameAndCity("aaaaa", "aaaaa");
  }
}
