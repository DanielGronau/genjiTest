package org.genji.generators.values;


import org.genji.TypeInfo;
import org.junit.jupiter.api.Test;

import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class UuidGenTest {
    private static final Random RANDOM = new Random();

    @Test
    void generate() {
        var list = UuidGen.INSTANCE.generate(RANDOM, new TypeInfo(UUID.class))
                                   .limit(50)
                                   .collect(Collectors.toList());
        assertThat(list).doesNotHaveDuplicates().doesNotContainNull();
    }
}