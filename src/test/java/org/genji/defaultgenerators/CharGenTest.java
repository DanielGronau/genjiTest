package org.genji.defaultgenerators;

import org.genji.annotations.CharSpec;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;
import java.util.Set;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CharGenTest {

    private static final Random RANDOM = new Random();

    @Test
    void generate() {
        var list  = CharGen.INSTANCE
                       .generate(RANDOM, List.of())
                       .limit(50)
                       .collect(toList());
        for (char c : list) {
            assertTrue(StringGenTest.DEFAULT_CHARS.contains("" + c));
        }
    }

    @Test
    @CharSpec(charSet = "xyz")
    void generate_charSet() throws Exception {
        var charSpec = CharGenTest.class
                           .getDeclaredMethod("generate_charSet")
                           .getAnnotation(CharSpec.class);
        var set = CharGen.INSTANCE
                      .generate(RANDOM, List.of(charSpec))
                      .limit(50)
                      .collect(toSet());
        assertEquals(Set.of('x', 'y', 'z'), set);
    }
}