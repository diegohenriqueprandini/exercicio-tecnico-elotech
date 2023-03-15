package com.diego.prandini.exerciciotecnicoelotech.utils;

import java.time.Duration;
import java.time.Instant;

public class TestUtils {

    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void doUntil(BooleanRunnable runnable, long timeout) {
        doUntil(runnable, timeout, "");
    }

    public static void doUntil(BooleanRunnable runnable, long timeout, String describe) {
        boolean result = false;
        Instant start = Instant.now();
        while (!result) {
            Instant end = Instant.now();
            Duration timeElapsed = Duration.between(start, end);
            if (timeElapsed.toMillis() > timeout)
                throw new RuntimeException("Application executor timeout: " + describe);
            sleep(500);
            try {
                result = runnable.test();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public interface BooleanRunnable {
        boolean test() throws Exception;
    }
}
