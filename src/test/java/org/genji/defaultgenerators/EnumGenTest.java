package org.genji.defaultgenerators;

import org.genji.GeneratorResolver;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertTrue;

enum TestEnum implements Cloneable {
    FOO,
    BAR,
    BAZ
}

class EnumGenTest {

    private static final Random RANDOM = new Random();

    @Test
    void generate() {
        var list =
            GeneratorResolver.resolve(TestEnum.class).get()
                             .generate(RANDOM, List.of())
                             .limit(50)
                             .collect(Collectors.toList());
        assertTrue(list.stream().filter(e -> e == TestEnum.FOO).count() > 2);
        assertTrue(list.stream().filter(e -> e == TestEnum.BAR).count() > 2);
        assertTrue(list.stream().filter(e -> e == TestEnum.BAZ).count() > 2);
    }

}