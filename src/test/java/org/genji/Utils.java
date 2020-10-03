package org.genji;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import static org.junit.jupiter.api.Assertions.assertTrue;

public final class Utils {

    public static <T extends Comparable<T>> void assertBetween(T actual, T lowerBound, T upperBound) {
        assertTrue(actual.compareTo(lowerBound) >= 0);
        assertTrue(actual.compareTo(upperBound) <= 0);
    }

    public static ParameterizedType getParametrizedType(Class<?> rawType, Type... parameterTypes) {
        return new ParameterizedType() {
            @Override
            public Type[] getActualTypeArguments() {
                return parameterTypes;
            }

            @Override
            public Type getRawType() {
                return rawType;
            }

            @Override
            public Type getOwnerType() {
                return null;
            }
        };
    }
}
