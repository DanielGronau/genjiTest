package org.genji.defaultgenerators;

import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Objects;
import java.util.Random;

import static org.genji.Utils.assertBetween;

class WithNullGenTest {
    private static final Random RANDOM = new Random();

    @Test
    void generate() {
        long nulls = WithNullGen.withNulls(0.2, new StringGen())
                                .generate(RANDOM, Collections.emptyList())
                                .limit(50)
                                .filter(Objects::isNull)
                                .count();
        assertBetween(nulls, 2L, 48L);
    }
}