package com.uplift.backend.util;

public class ValidationUtils {

    public static void require(boolean condition, String message) {
        if (!condition) {
            throw new IllegalArgumentException(message);
        }
    }
}
