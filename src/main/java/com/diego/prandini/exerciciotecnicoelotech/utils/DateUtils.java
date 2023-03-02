package com.diego.prandini.exerciciotecnicoelotech.utils;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtils {

    public static final String LOCAL_DATE_PATTERN = "yyyy-MM-dd";
    public static final DateTimeFormatter LOCAL_DATE_FORMATTER = DateTimeFormatter.ofPattern(LOCAL_DATE_PATTERN);

    public static final String LOCAL_DATE_TIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS";
    public static final DateTimeFormatter LOCAL_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(LOCAL_DATE_TIME_PATTERN);

    public static String toString(LocalDate date) {
        return LOCAL_DATE_FORMATTER.format(date);
    }

    public static String toString(LocalDateTime dateTime) {
        return LOCAL_DATE_TIME_FORMATTER.format(dateTime);
    }

    public static LocalDate toLocalDate(String stringValue) {
        return LocalDate.parse(stringValue, LOCAL_DATE_FORMATTER);
    }

    public static LocalDateTime toLocalDateTime(String stringValue) {
        return LocalDateTime.parse(stringValue, LOCAL_DATE_TIME_FORMATTER);
    }

    public static SimpleDateFormat createSimpleLocalDateTimeFormatter() {
        return new SimpleDateFormat(LOCAL_DATE_TIME_PATTERN);
    }

}
