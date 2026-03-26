package com.uplift.backend.util;

import java.security.SecureRandom;

public class OtpGenerator {

    private static final SecureRandom RANDOM = new SecureRandom();

    public static String generate6DigitCode() {
        int code = 100000 + RANDOM.nextInt(900000); // 100000–999999
        return Integer.toString(code);
    }
}
