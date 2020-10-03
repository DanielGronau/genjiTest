package org.genji.defaultgenerators;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BoolGenTest {

    private static final Random RANDOM = new Random();

    @Test
    void generate() {
        var set = new BoolGen().generate(RANDOM, List.of()).limit(50).collect(Collectors.toSet());
        assertEquals(Set.of(true, false), set);
    }
}
