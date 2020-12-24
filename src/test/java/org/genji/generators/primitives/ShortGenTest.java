package org.genji.generators.primitives;

import org.genji.TypeInfo;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Random;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static org.assertj.core.api.Assertions.assertThat;

class ShortGenTest {

    private static final Random RANDOM = new Random();

    @Test
    void generate() {
        var list = ShortGen.INSTANCE
                       .generate(RANDOM, new TypeInfo(Short.class))
                       .limit(50)
                       .collect(toList());

        assertThat(list.stream().anyMatch(i -> i < 0)).isTrue();
        assertThat(list.stream().anyMatch(i -> i > 0)).isTrue();
        assertThat(list.stream().anyMatch(i -> Math.abs(i) > 100)).isTrue();
    }

    @Test
    @ShortSpec(oneOf = {2, 5, 7})
    void generate_OneOf() throws Exception {
        var shortSpec = ShortGenTest.class
                            .getDeclaredMethod("generate_OneOf")
                            .getAnnotation(ShortSpec.class);
        var set = ShortGen.INSTANCE
                      .generate(RANDOM, new TypeInfo(Short.class, Map.of(ShortSpec.class, shortSpec)))
                      .limit(50)
                      .collect(toSet());

        assertThat(set).containsExactlyInAnyOrder((short) 2, (short) 5, (short) 7);
    }

    @Test
    @ShortSpec(from = 3, to = 15)
    void generate_FromTo() throws Exception {
        var shortSpec = ShortGenTest.class
                            .getDeclaredMethod("generate_FromTo")
                            .getAnnotation(ShortSpec.class);
        var list = ShortGen.INSTANCE
                       .generate(RANDOM, new TypeInfo(Short.class, Map.of(ShortSpec.class, shortSpec)))
                       .limit(50)
                       .collect(toList());

        assertThat(list.stream().allMatch(i -> i >= 3)).isTrue();
        assertThat(list.stream().allMatch(i -> i <= 15)).isTrue();
        assertThat(list.stream().anyMatch(i -> 5 < i && i < 12)).isTrue();
    }

}