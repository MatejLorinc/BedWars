package me.math3w.bedwars.utils;

import java.util.Arrays;
import java.util.stream.Collectors;

public class StringUtils {
    private StringUtils() {
        throw new IllegalStateException("Utility class cannot be instantiated");
    }

    public static String secondsToTime(int seconds) {
        return String.format("%02d:%02d", seconds / 60, seconds % 60);
    }

    public static String toCamelCaseWithSpace(String text) {
        return Arrays.stream(text.split("_"))
                .map(word -> word.isEmpty() ? word : Character.toUpperCase(word.charAt(0)) + word.substring(1).toLowerCase())
                .collect(Collectors.joining(" "));
    }
}
