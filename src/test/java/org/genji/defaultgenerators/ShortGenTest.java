package org.genji.defaultgenerators;

import org.genji.annotations.ShortSpec;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;
import java.util.Set;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ShortGenTest {

    private static final Random RANDOM = new Random();

    @Test
    void generate() {
        var list = ShortGen.INSTANCE
                       .generate(RANDOM, List.of())
                       .limit(50)
                       .collect(toList());

        assertTrue(list.stream().anyMatch(i -> i < 0));
        assertTrue(list.stream().anyMatch(i -> i > 0));
        assertTrue(list.stream().anyMatch(i -> Math.abs(i) > 100));
    }

    @Test
    @ShortSpec(oneOf = {2, 5, 7})
    void generate_OneOf() throws Exception {
        var intSpec = ShortGenTest.class
                          .getDeclaredMethod("generate_OneOf")
                          .getAnnotation(ShortSpec.class);
        var set = ShortGen.INSTANCE
                      .generate(RANDOM, List.of(intSpec))
                      .limit(50)
                      .collect(toSet());

        assertEquals(Set.of((short) 2, (short) 5, (short) 7), set);
    }

    @Test
    @ShortSpec(from = 3, to = 15)
    void generate_FromTo() throws Exception {
        var intSpec = ShortGenTest.class
                          .getDeclaredMethod("generate_FromTo")
                          .getAnnotation(ShortSpec.class);
        var list = ShortGen.INSTANCE
                       .generate(RANDOM, List.of(intSpec))
                       .limit(50)
                       .collect(toList());

        assertTrue(list.stream().allMatch(i -> i >= 3));
        assertTrue(list.stream().allMatch(i -> i <= 15));
        assertTrue(list.stream().anyMatch(i -> 5 < i && i < 12));
    }

}