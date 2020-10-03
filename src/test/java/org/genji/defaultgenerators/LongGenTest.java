package org.genji.defaultgenerators;

import org.genji.annotations.LongSpec;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;
import java.util.Set;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LongGenTest {

    private static final Random RANDOM = new Random();

    @Test
    void generate() {
        var list = LongGen.INSTANCE
                       .generate(RANDOM, List.of())
                       .limit(50)
                       .collect(toList());

        assertTrue(list.stream().anyMatch(i -> i < 0));
        assertTrue(list.stream().anyMatch(i -> i > 0));
        assertTrue(list.stream().anyMatch(i -> Math.abs(i) > 100));
    }

    @Test
    @LongSpec(oneOf = {2L, 5L, 700000000000L})
    void generate_OneOf() throws Exception {
        var longSpec = LongGenTest.class
                             .getDeclaredMethod("generate_OneOf")
                             .getAnnotation(LongSpec.class);
        var set = LongGen.INSTANCE
                       .generate(RANDOM, List.of(longSpec))
                       .limit(50)
                       .collect(toSet());

        assertEquals(Set.of(2L, 5L, 700000000000L), set);
    }

    @Test
    @LongSpec(from = 3L, to = 15L)
    void generate_FromTo() throws Exception {
        var longSpec = LongGenTest.class
                          .getDeclaredMethod("generate_FromTo")
                          .getAnnotation(LongSpec.class);
        var list = LongGen.INSTANCE
                      .generate(RANDOM, List.of(longSpec))
                      .limit(50)
                      .collect(toList());

        assertTrue(list.stream().allMatch(i -> i >= 3L));
        assertTrue(list.stream().allMatch(i -> i <= 15L));
        assertTrue(list.stream().anyMatch(i -> 5L < i && i < 12L));
    }

}