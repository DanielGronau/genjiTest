package org.genji.defaultgenerators;

import org.genji.GeneratorResolver;
import org.genji.TypeInfo;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class EnumGenTest {

    private static final Random RANDOM = new Random();

    @Test
    void generate() {
        var list =
            GeneratorResolver.resolve(TestEnum.class, Map.of()).get()
                             .generate(RANDOM, new TypeInfo(TestEnum.class))
                             .limit(50)
                             .collect(Collectors.toList());
        assertThat(list.stream().filter(e -> e == TestEnum.FOO).count() > 2).isTrue();
        assertThat(list.stream().filter(e -> e == TestEnum.BAR).count() > 2).isTrue();
        assertThat(list.stream().filter(e -> e == TestEnum.BAZ).count() > 2).isTrue();
    }

}