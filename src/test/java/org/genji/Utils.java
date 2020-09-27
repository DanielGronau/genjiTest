package org.genji;

import static org.junit.jupiter.api.Assertions.assertTrue;

public final class Utils {

    public static <T extends Comparable<T>> void assertBetween(T actual, T lowerBound, T upperBound) {
        assertTrue(actual.compareTo(lowerBound) >= 0);
        assertTrue(actual.compareTo(upperBound) <= 0);
    }
}
