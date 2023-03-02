package com.diego.prandini.exerciciotecnicoelotech.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class JsonUtils {

    private static final ObjectMapper OBJECT_MAPPER = defaultJackson2ObjectMapperBuilder().build();

    public static Jackson2ObjectMapperBuilder defaultJackson2ObjectMapperBuilder() {
        return new Jackson2ObjectMapperBuilder()
                .serializationInclusion(JsonInclude.Include.ALWAYS)
                .failOnEmptyBeans(false)
                .failOnUnknownProperties(false)
                .serializerByType(LocalDateTime.class, new LocalDateTimeSerializer(DateUtils.LOCAL_DATE_TIME_FORMATTER))
                .serializerByType(LocalDate.class, new LocalDateSerializer(DateUtils.LOCAL_DATE_FORMATTER))
                .dateFormat(DateUtils.createSimpleLocalDateTimeFormatter())
                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    public static String toJson(Object object) {
        try {
            return OBJECT_MAPPER.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new JsonUtilsException(e);
        }
    }

    public static <T> T fromJson(String jsonObject, Class<T> type) {
        try {
            return OBJECT_MAPPER.readValue(jsonObject, type);
        } catch (JsonProcessingException e) {
            throw new JsonUtilsException(e);
        }
    }

    public static <T> T fromJson(String jsonObject, TypeReference<T> valueTypeRef) {
        try {
            return OBJECT_MAPPER.readValue(jsonObject, valueTypeRef);
        } catch (JsonProcessingException e) {
            throw new JsonUtilsException(e);
        }
    }

    public static class JsonUtilsException extends RuntimeException {
        public JsonUtilsException(Exception e) {
            super(e);
        }
    }
}
