package com.in726.app.parser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.text.SimpleDateFormat;

/**
 * Class for parsing data by class of instance from JSON format.
 */
public class EasyJsonParser {
    private static ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Method for parsing JSON data by the model class.
     *
     * @param json data to parse
     * @param type type of receiving object
     * @param <T>  model class to receive
     * @return parsed object by model class
     * @throws JsonProcessingException exception of parsing
     */
    public static <T> T parseJsonToObject(String json, Class<T> type) throws JsonProcessingException {
        var t = new ObjectMapper().readValue(json, type);
        return t;
    }

    /**
     * Method for parsing JSON data by the model class.
     *
     * @param json             data to parse
     * @param type             type of receiving object
     * @param <T>              model class to receive
     * @param simpleDateFormat format for parsing date in th JSON data
     * @return parsed object by model class
     * @throws JsonProcessingException exception of parsing
     */
    public static <T> T parseJsonToObject(String json, Class<T> type, SimpleDateFormat simpleDateFormat)
            throws JsonProcessingException {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.setDateFormat(simpleDateFormat);
        var t = objectMapper.readValue(json, type);
        return t;
    }
}
