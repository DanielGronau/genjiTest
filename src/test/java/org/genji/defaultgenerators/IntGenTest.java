package org.genji.defaultgenerators;

import org.genji.TypeInfo;
import org.genji.annotations.IntSpec;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Random;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static org.assertj.core.api.Assertions.assertThat;

class IntGenTest {

    private static final Random RANDOM = new Random();

    @Test
    void generate() {
        var list = IntGen.INSTANCE
                       .generate(RANDOM, new TypeInfo(Integer.class))
                       .limit(50)
                       .collect(toList());

        assertThat(list.stream().anyMatch(i -> i < 0)).isTrue();
        assertThat(list.stream().anyMatch(i -> i > 0)).isTrue();
        assertThat(list.stream().anyMatch(i -> Math.abs(i) > 100)).isTrue();
    }

    @Test
    @IntSpec(oneOf = {2, 5, 7})
    void generate_OneOf() throws Exception {
        var intSpec = IntGenTest.class
                             .getDeclaredMethod("generate_OneOf")
                             .getAnnotation(IntSpec.class);
        var set = IntGen.INSTANCE
                       .generate(RANDOM, new TypeInfo(Integer.class, Map.of(IntSpec.class, intSpec)))
                       .limit(50)
                       .collect(toSet());

        assertThat(set).containsExactlyInAnyOrder(2, 5, 7);
    }

    @Test
    @IntSpec(from = 3, to = 15)
    void generate_FromTo() throws Exception {
        var intSpec = IntGenTest.class
                          .getDeclaredMethod("generate_FromTo")
                          .getAnnotation(IntSpec.class);
        var list = IntGen.INSTANCE
                      .generate(RANDOM, new TypeInfo(Integer.class, Map.of(IntSpec.class, intSpec)))
                      .limit(50)
                      .collect(toList());

        assertThat(list.stream().allMatch(i -> i >= 3)).isTrue();
        assertThat(list.stream().allMatch(i -> i <= 15)).isTrue();
        assertThat(list.stream().anyMatch(i -> 5 < i && i < 12)).isTrue();
    }

}