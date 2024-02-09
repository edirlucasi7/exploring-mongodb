package com.example.mongodb.utils;

public class StringUtils {

    public static String removeExtraEmptySpacesAndLines(String value) {
        String valueWithoutLines = value.replaceAll("\\n+", " ");
        return removeExtraEmptySpaces(valueWithoutLines);
    }

    private static String removeExtraEmptySpaces(String value) {
        return value.trim().replaceAll("\u00A0"," ").replaceAll("\\s+", " ");
    }
}
