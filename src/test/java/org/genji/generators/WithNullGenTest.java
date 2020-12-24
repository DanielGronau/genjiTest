package org.genji.generators;

import org.genji.TypeInfo;
import org.genji.generators.values.StringGen;
import org.junit.jupiter.api.Test;

import java.util.Objects;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

class WithNullGenTest {
    private static final Random RANDOM = new Random();

    @Test
    void generate() {
        long nulls = WithNullGen.withNulls(0.2, StringGen.INSTANCE)
                                .generate(RANDOM, new TypeInfo(String.class))
                                .limit(50)
                                .filter(Objects::isNull)
                                .count();
        assertThat(nulls).isBetween(2L, 48L);
    }
}