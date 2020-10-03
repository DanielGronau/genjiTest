package org.genji.defaultgenerators;

import org.genji.annotations.ByteSpec;
import org.genji.annotations.IntSpec;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;
import java.util.Set;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ByteGenTest {

    private static final Random RANDOM = new Random();

    @Test
    void generate() {
        var list = ByteGen.INSTANCE
                       .generate(RANDOM, List.of())
                       .limit(50)
                       .collect(toList());

        assertTrue(list.stream().anyMatch(i -> i < 0));
        assertTrue(list.stream().anyMatch(i -> i > 0));
        assertTrue(list.stream().anyMatch(i -> Math.abs(i) > 50));
    }

    @Test
    @ByteSpec(oneOf = {2, 5, 7})
    void generate_OneOf() throws Exception {
        var byteSpec = ByteGenTest.class
                          .getDeclaredMethod("generate_OneOf")
                          .getAnnotation(ByteSpec.class);
        var set = ByteGen.INSTANCE
                      .generate(RANDOM, List.of(byteSpec))
                      .limit(50)
                      .collect(toSet());

        assertEquals(Set.of((byte) 2, (byte) 5, (byte) 7), set);
    }

    @Test
    @ByteSpec(from = 3, to = 15)
    void generate_FromTo() throws Exception {
        var byteSpec = ByteGenTest.class
                          .getDeclaredMethod("generate_FromTo")
                          .getAnnotation(ByteSpec.class);
        var list = ByteGen.INSTANCE
                       .generate(RANDOM, List.of(byteSpec))
                       .limit(50)
                       .collect(toList());

        assertTrue(list.stream().allMatch(i -> i >= 3));
        assertTrue(list.stream().allMatch(i -> i <= 15));
        assertTrue(list.stream().anyMatch(i -> 5 < i && i < 12));
    }

}