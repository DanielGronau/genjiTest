package org.genji.defaultgenerators;

import org.genji.annotations.DoubleSpec;
import org.genji.annotations.IntSpec;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;
import java.util.Set;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DoubleGenTest {

    private static final Random RANDOM = new Random();

    @Test
    void generate() {
        var list = DoubleGen.INSTANCE
                       .generate(RANDOM, List.of())
                       .limit(50)
                       .collect(toList());

        System.out.println(list);
        assertTrue(list.stream().anyMatch(i -> i < 0));
        assertTrue(list.stream().anyMatch(i -> i > 0));
        assertTrue(list.stream().anyMatch(i -> Math.abs(i) > 100));
    }

    @Test
    @DoubleSpec(oneOf = {2, 5, 7, Double.NEGATIVE_INFINITY, Double.NaN})
    void generate_OneOf() throws Exception {
        var doubleSpec = DoubleGenTest.class
                             .getDeclaredMethod("generate_OneOf")
                             .getAnnotation(DoubleSpec.class);
        var set = DoubleGen.INSTANCE
                       .generate(RANDOM, List.of(doubleSpec))
                       .limit(50)
                       .collect(toSet());

        assertEquals(Set.of(2.0, 5.0, 7.0, Double.NEGATIVE_INFINITY, Double.NaN), set);
    }

    @Test
    @DoubleSpec(from = 3, to = 15)
    void generate_FromTo() throws Exception {
        var doubleSpec = DoubleGenTest.class
                          .getDeclaredMethod("generate_FromTo")
                          .getAnnotation(DoubleSpec.class);
        var list = DoubleGen.INSTANCE
                      .generate(RANDOM, List.of(doubleSpec))
                      .limit(50)
                      .collect(toList());

        assertTrue(list.stream().allMatch(i -> i >= 3));
        assertTrue(list.stream().allMatch(i -> i <= 15));
        assertTrue(list.stream().anyMatch(i -> 5 < i && i < 12));
    }

}