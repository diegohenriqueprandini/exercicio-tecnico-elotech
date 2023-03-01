package com.diego.prandini.exerciciotecnicoelotech.utils;

public class StringUtils {

    public static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    public static String trimToEmpty(String value) {
        if (value == null || value.isBlank())
            return "";
        return value;
    }
}
