package org.genji.defaultgenerators;

import org.genji.TypeInfo;
import org.genji.annotations.ByteSpec;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Random;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static org.assertj.core.api.Assertions.assertThat;

class ByteGenTest {

    private static final Random RANDOM = new Random();

    @Test
    void generate() {
        var list = ByteGen.INSTANCE
                       .generate(RANDOM, new TypeInfo(Byte.class))
                       .limit(50)
                       .collect(toList());

        assertThat(list.stream().anyMatch(i -> i < 0)).isTrue();
        assertThat(list.stream().anyMatch(i -> i > 0)).isTrue();
        assertThat(list.stream().anyMatch(i -> Math.abs(i) > 50)).isTrue();
    }

    @Test
    @ByteSpec(oneOf = {2, 5, 7})
    void generate_OneOf() throws Exception {
        var byteSpec = ByteGenTest.class
                           .getDeclaredMethod("generate_OneOf")
                           .getAnnotation(ByteSpec.class);
        var set = ByteGen.INSTANCE
                      .generate(RANDOM, new TypeInfo(Byte.class, Map.of(ByteSpec.class, byteSpec)))
                      .limit(50)
                      .collect(toSet());

        assertThat(set).containsExactlyInAnyOrder((byte) 2, (byte) 5, (byte) 7);
    }

    @Test
    @ByteSpec(from = 3, to = 15)
    void generate_FromTo() throws Exception {
        var byteSpec = ByteGenTest.class
                           .getDeclaredMethod("generate_FromTo")
                           .getAnnotation(ByteSpec.class);
        var list = ByteGen.INSTANCE
                       .generate(RANDOM, new TypeInfo(Byte.class, Map.of(ByteSpec.class, byteSpec)))
                       .limit(50)
                       .collect(toList());

        assertThat(list.stream().allMatch(i -> i >= 3)).isTrue();
        assertThat(list.stream().allMatch(i -> i <= 15)).isTrue();
        assertThat(list.stream().anyMatch(i -> 5 < i && i < 12)).isTrue();
    }

}