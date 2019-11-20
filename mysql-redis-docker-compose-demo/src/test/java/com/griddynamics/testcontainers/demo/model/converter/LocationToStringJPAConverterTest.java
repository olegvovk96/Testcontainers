package com.griddynamics.testcontainers.demo.model.converter;

import static org.junit.Assert.assertEquals;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.griddynamics.testcontainers.demo.model.Location;
import com.griddynamics.testcontainers.demo.model.converter.LocationToStringJPAConverter;
import org.junit.Test;

public class LocationToStringJPAConverterTest {
  @Test
  public void testconvertToDatabaseColumn() throws Exception {
    // Arrange
    LocationToStringJPAConverter locationToStringJPAConverter = new LocationToStringJPAConverter(new ObjectMapper());

    // Act and Assert
    assertEquals("{\"country\":null,\"city\":null}",
        locationToStringJPAConverter.convertToDatabaseColumn(new Location()));
  }
}
