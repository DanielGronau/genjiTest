package org.genji.defaultgenerators;

import org.genji.annotations.OptionalSpec;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static org.genji.Utils.assertBetween;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OptionalGenTest {

    private static final Random RANDOM = new Random();

    @Test
    @SuppressWarnings({"unchecked", "rawtypes"})
    void generate() {
        List<Optional<String>> lists =
            (List) OptionalGen.INSTANCE
                       .generate(RANDOM, List.of(), String.class)
                       .limit(50)
                       .collect(toList());

        assertTrue(lists.contains(Optional.<String>empty()));
        for (Optional<String> optional : lists) {
            optional.ifPresent(s -> {
                assertBetween(s.length(), 0, 20);
                for (char c : s.toCharArray())
                    assertTrue(StringGenTest.DEFAULT_CHARS.contains("" + c));
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
                       .generate(RANDOM, List.of(optionalSpec), String.class)
                       .limit(50)
                       .collect(toSet());

        assertEquals(Set.of(Optional.empty()), set);
    }

}