package org.genji.generators.primitives;

import org.genji.TypeInfo;
import org.genji.generators.primitives.BoolGen;
import org.junit.jupiter.api.Test;

import java.util.Random;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class BoolGenTest {

    private static final Random RANDOM = new Random();

    @Test
    void generate() {
        var set = BoolGen.INSTANCE.generate(RANDOM, new TypeInfo(Boolean.class)).limit(50).collect(Collectors.toSet());
        assertThat(set).containsExactlyInAnyOrder(true, false);
    }
}
