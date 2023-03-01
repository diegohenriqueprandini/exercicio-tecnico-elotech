package com.diego.prandini.exerciciotecnicoelotech.utils;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class DateUtils {

    public static final DateTimeFormatter LOCAL_DATE_FORMATTER = DateTimeFormatter
            .ofPattern("yyyy-MM-dd")
            .withZone(ZoneId.systemDefault());

    public static String toString(LocalDate date) {
        return LOCAL_DATE_FORMATTER.format(date);
    }

    public static LocalDate toLocalDate(String stringValue) {
        return LocalDate.parse(stringValue, LOCAL_DATE_FORMATTER);
    }

}
