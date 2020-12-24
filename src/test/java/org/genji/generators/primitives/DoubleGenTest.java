package org.genji.generators.primitives;

import org.genji.TypeInfo;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Random;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static org.assertj.core.api.Assertions.assertThat;

class DoubleGenTest {

    private static final Random RANDOM = new Random();

    @Test
    void generate() {
        var list = DoubleGen.INSTANCE
                       .generate(RANDOM, new TypeInfo(Double.class))
                       .limit(50)
                       .collect(toList());

        assertThat(list.stream().anyMatch(i -> i < 0)).isTrue();
        assertThat(list.stream().anyMatch(i -> i > 0)).isTrue();
        assertThat(list.stream().anyMatch(i -> Math.abs(i) > 100)).isTrue();
    }

    @Test
    @DoubleSpec(oneOf = {2, 5, 7, Double.NEGATIVE_INFINITY, Double.NaN})
    void generate_OneOf() throws Exception {
        var doubleSpec = DoubleGenTest.class
                             .getDeclaredMethod("generate_OneOf")
                             .getAnnotation(DoubleSpec.class);
        var set = DoubleGen.INSTANCE
                       .generate(RANDOM, new TypeInfo(Double.class, Map.of(DoubleSpec.class, doubleSpec)))
                       .limit(50)
                       .collect(toSet());

        assertThat(set).containsExactlyInAnyOrder(2.0, 5.0, 7.0, Double.NEGATIVE_INFINITY, Double.NaN);
    }

    @Test
    @DoubleSpec(from = 3, to = 15)
    void generate_FromTo() throws Exception {
        var doubleSpec = DoubleGenTest.class
                          .getDeclaredMethod("generate_FromTo")
                          .getAnnotation(DoubleSpec.class);
        var list = DoubleGen.INSTANCE
                      .generate(RANDOM, new TypeInfo(Double.class, Map.of(DoubleSpec.class, doubleSpec)))
                      .limit(50)
                      .collect(toList());

        assertThat(list.stream().allMatch(i -> i >= 3)).isTrue();
        assertThat(list.stream().allMatch(i -> i <= 15)).isTrue();
        assertThat(list.stream().anyMatch(i -> 5 < i && i < 12)).isTrue();
    }

}