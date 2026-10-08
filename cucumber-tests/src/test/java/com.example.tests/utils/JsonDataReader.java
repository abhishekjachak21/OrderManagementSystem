package com.example.tests.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class JsonDataReader {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static Map<String, Object> readJson(String fileName) throws IOException {

        try (InputStream inputStream =
                     JsonDataReader.class
                             .getClassLoader()
                             .getResourceAsStream("testdata/" + fileName)) {

            return objectMapper.readValue(inputStream, Map.class);
        }
    }
}