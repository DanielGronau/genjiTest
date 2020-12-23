package org.genji.defaultgenerators;

import org.genji.TypeInfo;
import org.genji.annotations.CharSpec;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Random;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static org.assertj.core.api.Assertions.assertThat;

class CharGenTest {

    private static final Random RANDOM = new Random();

    @Test
    void generate() {
        var list  = CharGen.INSTANCE
                       .generate(RANDOM, new TypeInfo(Character.class))
                       .limit(50)
                       .collect(toList());
        for (char c : list) {
            assertThat(StringGenTest.DEFAULT_CHARS.indexOf("" + c)).isNotEqualTo(-1);
        }
    }

    @Test
    @CharSpec(charSet = "xyz")
    void generate_charSet() throws Exception {
        var charSpec = CharGenTest.class
                           .getDeclaredMethod("generate_charSet")
                           .getAnnotation(CharSpec.class);
        var set = CharGen.INSTANCE
                      .generate(RANDOM, new TypeInfo(Character.class, Map.of(CharSpec.class, charSpec)))
                      .limit(50)
                      .collect(toSet());
        assertThat(set).containsExactlyInAnyOrder('x', 'y', 'z');
    }
}