package com.griddynamics.testcontainers.demo.model.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.griddynamics.testcontainers.demo.model.Location;
import lombok.RequiredArgsConstructor;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.io.IOException;

@Converter
@RequiredArgsConstructor
public class LocationToStringJPAConverter implements AttributeConverter<Location, String> {

    private final ObjectMapper objectMapper;

    @Override
    public String convertToDatabaseColumn(Location location) {
        try {
            return objectMapper.writeValueAsString(location);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    @Override
    public Location convertToEntityAttribute(String attribute) {
        try {
            return objectMapper.readValue(attribute, Location.class);
        } catch (IOException e) {
            return null;
        }
    }
}
