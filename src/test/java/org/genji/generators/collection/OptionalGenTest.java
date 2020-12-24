package org.genji.generators.collection;

import org.genji.TypeInfo;
import org.genji.generators.values.OptionalSpec;
import org.genji.generators.values.StringGenTest;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static org.assertj.core.api.Assertions.assertThat;

class OptionalGenTest {

    private static final Random RANDOM = new Random();

    @Test
    @SuppressWarnings({"unchecked", "rawtypes"})
    void generate() {
        List<Optional<String>> lists =
            (List) OptionalGen.INSTANCE
                       .generate(RANDOM, new TypeInfo(Optional.class, Map.of(), List.of(new TypeInfo(String.class))))
                       .limit(50)
                       .collect(toList());

        assertThat(lists).contains(Optional.<String>empty());
        for (Optional<String> optional : lists) {
            optional.ifPresent(s -> {
                assertThat(s.length()).isBetween(0, 20);
                for (char c : s.toCharArray())
                    assertThat(StringGenTest.DEFAULT_CHARS.indexOf("" + c)).isNotEqualTo(-1);
            });
        }
    }

    @Test
    @OptionalSpec(probabilityForEmpty = 1.0)
    void generate_onlyEmpty() throws Exception {
        var optionalSpec = OptionalGenTest.class
                          .getDeclaredMethod("generate_onlyEmpty")
                          .getAnnotation(OptionalSpec.class);
        var set =
            OptionalGen.INSTANCE
                       .generate(RANDOM, new TypeInfo(Optional.class, Map.of(OptionalSpec.class, optionalSpec), List.of(new TypeInfo(String.class))))
                       .limit(50)
                       .collect(toSet());

        assertThat(set).containsExactly(Optional.empty());
    }

}