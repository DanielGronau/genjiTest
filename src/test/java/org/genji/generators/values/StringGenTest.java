package org.genji.generators.values;

import org.genji.TypeInfo;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Random;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static org.assertj.core.api.Assertions.assertThat;

public class StringGenTest {
    private static final Random RANDOM = new Random();

    public static final String DEFAULT_CHARS = " \0\t\n\r\\\"'²³?!#+*/;.-_<>|§$%&1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ[]{}";

    @Test
    void generate() {
        var list = StringGen.INSTANCE
                       .generate(RANDOM, new TypeInfo(String.class))
                       .limit(50)
                       .collect(toList());
        for (String s : list) {
            assertThat(s.length()).isBetween(0, 20);
            for (char c : s.toCharArray())
                assertThat(DEFAULT_CHARS.indexOf("" + c)).isNotEqualTo(-1);
        }
    }

    @Test
    @StringSpec(from = 3, to = 6)
    void generate_withStringLength() throws Exception {
        var stringSpec = StringGenTest.class
                             .getDeclaredMethod("generate_withStringLength")
                             .getAnnotation(StringSpec.class);
        var list = StringGen.INSTANCE
                       .generate(RANDOM, new TypeInfo(String.class, Map.of(StringSpec.class, stringSpec)))
                       .limit(50)
                       .collect(toList());
        for (String s : list) {
            assertThat(s.length()).isBetween(3, 6);
            for (char c : s.toCharArray())
                assertThat(DEFAULT_CHARS.indexOf("" + c)).isNotEqualTo(-1);
        }
    }

    @Test
    @StringSpec(charSet = "abc")
    void generate_withCharSet() throws Exception {
        var stringSpec = StringGenTest.class
                             .getDeclaredMethod("generate_withCharSet")
                             .getAnnotation(StringSpec.class);
        var list = StringGen.INSTANCE
                       .generate(RANDOM, new TypeInfo(String.class, Map.of(StringSpec.class, stringSpec)))
                       .limit(50)
                       .collect(toList());
        for (String s : list) {
            assertThat(s.length()).isBetween(0, 20);
            for (char c : s.toCharArray())
                assertThat("abc".indexOf("" + c)).isNotEqualTo(-1);
        }
    }

    @Test
    @StringSpec(charSet = "", oneOf = {"foo", "bar", "baz"})
    void generate_withOneOf() throws Exception {
        var stringSpec = StringGenTest.class
                             .getDeclaredMethod("generate_withOneOf")
                             .getAnnotation(StringSpec.class);
        var set = StringGen.INSTANCE
                      .generate(RANDOM, new TypeInfo(String.class, Map.of(StringSpec.class, stringSpec)))
                      .limit(50)
                      .collect(toSet());
        assertThat(set).containsExactlyInAnyOrder("foo", "bar", "baz");
    }
}