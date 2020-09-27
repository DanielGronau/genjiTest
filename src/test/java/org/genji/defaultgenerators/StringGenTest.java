package org.genji.defaultgenerators;

import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import static java.util.stream.Collectors.toList;
import static org.genji.Utils.assertBetween;
import static org.genji.defaultgenerators.StringGen.DEFAULT_CHARS;
import static org.genji.defaultgenerators.StringGen.DEFAULT_LENGTH;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StringGenTest {
    private static final Random RANDOM = new Random();

    @Test
    void generate() {
        List<String> list = new StringGen().generate(RANDOM, Collections.emptyList()).limit(50).collect(toList());
        for (String s : list) {
            assertBetween(s.length(), 0, DEFAULT_LENGTH);
            for (char c : s.toCharArray())
                assertTrue(DEFAULT_CHARS.contains("" + c));
        }
    }
}