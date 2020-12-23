package org.genji.defaultgenerators;

import org.genji.TypeInfo;
import org.genji.annotations.LongSpec;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Random;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static org.assertj.core.api.Assertions.assertThat;

class LongGenTest {

    private static final Random RANDOM = new Random();

    @Test
    void generate() {
        var list = LongGen.INSTANCE
                       .generate(RANDOM, new TypeInfo(Long.class))
                       .limit(50)
                       .collect(toList());

        assertThat(list.stream().anyMatch(i -> i < 0)).isTrue();
        assertThat(list.stream().anyMatch(i -> i > 0)).isTrue();
        assertThat(list.stream().anyMatch(i -> Math.abs(i) > 100)).isTrue();
    }

    @Test
    @LongSpec(oneOf = {2L, 5L, 700000000000L})
    void generate_OneOf() throws Exception {
        var longSpec = LongGenTest.class
                             .getDeclaredMethod("generate_OneOf")
                             .getAnnotation(LongSpec.class);
        var set = LongGen.INSTANCE
                       .generate(RANDOM, new TypeInfo(Long.class, Map.of(LongSpec.class, longSpec)))
                       .limit(50)
                       .collect(toSet());

        assertThat(set).containsExactlyInAnyOrder(2L, 5L, 700000000000L);
    }

    @Test
    @LongSpec(from = 3L, to = 15L)
    void generate_FromTo() throws Exception {
        var longSpec = LongGenTest.class
                          .getDeclaredMethod("generate_FromTo")
                          .getAnnotation(LongSpec.class);
        var list = LongGen.INSTANCE
                      .generate(RANDOM, new TypeInfo(Long.class, Map.of(LongSpec.class, longSpec)))
                      .limit(50)
                      .collect(toList());

        assertThat(list.stream().allMatch(i -> i >= 3L)).isTrue();
        assertThat(list.stream().allMatch(i -> i <= 15L)).isTrue();
        assertThat(list.stream().anyMatch(i -> 5L < i && i < 12L)).isTrue();
    }

}