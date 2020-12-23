package org.genji.defaultgenerators;

import org.genji.TypeInfo;
import org.genji.annotations.FloatSpec;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Random;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static org.assertj.core.api.Assertions.assertThat;

class FloatGenTest {

    private static final Random RANDOM = new Random();

    @Test
    void generate() {
        var list = FloatGen.INSTANCE
                       .generate(RANDOM, new TypeInfo(Float.class))
                       .limit(50)
                       .collect(toList());

        assertThat(list.stream().anyMatch(i -> i < 0)).isTrue();
        assertThat(list.stream().anyMatch(i -> i > 0)).isTrue();
        assertThat(list.stream().anyMatch(i -> Math.abs(i) > 100)).isTrue();
    }

    @Test
    @FloatSpec(oneOf = {2, 5, 7, Float.NEGATIVE_INFINITY, Float.NaN})
    void generate_OneOf() throws Exception {
        var floatSpec = FloatGenTest.class
                            .getDeclaredMethod("generate_OneOf")
                            .getAnnotation(FloatSpec.class);
        var set = FloatGen.INSTANCE
                      .generate(RANDOM, new TypeInfo(Float.class, Map.of(FloatSpec.class, floatSpec)))
                      .limit(50)
                      .collect(toSet());

        assertThat(set).containsExactlyInAnyOrder(2.0F, 5.0F, 7.0F, Float.NEGATIVE_INFINITY, Float.NaN);
    }

    @Test
    @FloatSpec(from = 3, to = 15)
    void generate_FromTo() throws Exception {
        var floatSpec = FloatGenTest.class
                            .getDeclaredMethod("generate_FromTo")
                            .getAnnotation(FloatSpec.class);
        var list = FloatGen.INSTANCE
                       .generate(RANDOM, new TypeInfo(Float.class, Map.of(FloatSpec.class, floatSpec)))
                       .limit(50)
                       .collect(toList());

        assertThat(list.stream().allMatch(i -> i >= 3)).isTrue();
        assertThat(list.stream().allMatch(i -> i <= 15)).isTrue();
        assertThat(list.stream().anyMatch(i -> 5 < i && i < 12)).isTrue();
    }

}