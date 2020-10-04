package org.genji.defaultgenerators;

import org.genji.annotations.FloatSpec;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;
import java.util.Set;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FloatGenTest {

    private static final Random RANDOM = new Random();

    @Test
    void generate() {
        var list = FloatGen.INSTANCE
                       .generate(RANDOM, List.of())
                       .limit(50)
                       .collect(toList());

        assertTrue(list.stream().anyMatch(i -> i < 0));
        assertTrue(list.stream().anyMatch(i -> i > 0));
        assertTrue(list.stream().anyMatch(i -> Math.abs(i) > 100));
    }

    @Test
    @FloatSpec(oneOf = {2, 5, 7, Float.NEGATIVE_INFINITY, Float.NaN})
    void generate_OneOf() throws Exception {
        var floatSpec = FloatGenTest.class
                            .getDeclaredMethod("generate_OneOf")
                            .getAnnotation(FloatSpec.class);
        var set = FloatGen.INSTANCE
                      .generate(RANDOM, List.of(floatSpec))
                      .limit(50)
                      .collect(toSet());

        assertEquals(Set.of(2.0F, 5.0F, 7.0F, Float.NEGATIVE_INFINITY, Float.NaN), set);
    }

    @Test
    @FloatSpec(from = 3, to = 15)
    void generate_FromTo() throws Exception {
        var floatSpec = FloatGenTest.class
                            .getDeclaredMethod("generate_FromTo")
                            .getAnnotation(FloatSpec.class);
        var list = FloatGen.INSTANCE
                       .generate(RANDOM, List.of(floatSpec))
                       .limit(50)
                       .collect(toList());

        assertTrue(list.stream().allMatch(i -> i >= 3));
        assertTrue(list.stream().allMatch(i -> i <= 15));
        assertTrue(list.stream().anyMatch(i -> 5 < i && i < 12));
    }

}