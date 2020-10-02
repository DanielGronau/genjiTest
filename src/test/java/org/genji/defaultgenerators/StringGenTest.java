package org.genji.defaultgenerators;

import org.genji.annotations.StringSpec;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;
import java.util.Set;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static org.genji.Utils.assertBetween;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StringGenTest {
    private static final Random RANDOM = new Random();

    private static final String DEFAULT_CHARS = " \0\t\n\r\\\"'²³?!#+*/;.-_<>|§$%&1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ[]{}";

    @Test
    void generate() {
        var list = new StringGen()
                       .generate(RANDOM, List.of())
                       .limit(50)
                       .collect(toList());
        for (String s : list) {
            assertBetween(s.length(), 0, 20);
            for (char c : s.toCharArray())
                assertTrue(DEFAULT_CHARS.contains("" + c));
        }
    }

    @Test
    @StringSpec(from = 3, to = 6)
    void generate_withStringLength() throws Exception {
        var stringSpec = StringGenTest.class
                             .getDeclaredMethod("generate_withStringLength")
                             .getAnnotation(StringSpec.class);
        var list = new StringGen()
                       .generate(RANDOM, List.of(stringSpec))
                       .limit(50)
                       .collect(toList());
        for (String s : list) {
            assertBetween(s.length(), 3, 6);
            for (char c : s.toCharArray())
                assertTrue(DEFAULT_CHARS.contains("" + c));
        }
    }

    @Test
    @StringSpec(charSet = "abc")
    void generate_withCharSet() throws Exception {
        var stringSpec = StringGenTest.class
                             .getDeclaredMethod("generate_withCharSet")
                             .getAnnotation(StringSpec.class);
        var list = new StringGen()
                       .generate(RANDOM, List.of(stringSpec))
                       .limit(50)
                       .collect(toList());
        for (String s : list) {
            assertBetween(s.length(), 0, 20);
            for (char c : s.toCharArray())
                assertTrue("abc".contains("" + c));
        }
    }

    @Test
    @StringSpec(charSet = "", oneOf = {"foo", "bar", "baz"})
    void generate_withOneOf() throws Exception {
        var stringSpec = StringGenTest.class
                             .getDeclaredMethod("generate_withOneOf")
                             .getAnnotation(StringSpec.class);
        var set = new StringGen()
                      .generate(RANDOM, List.of(stringSpec))
                      .limit(50)
                      .collect(toSet());
        assertEquals(Set.of("foo", "bar", "baz"), set);
    }
}