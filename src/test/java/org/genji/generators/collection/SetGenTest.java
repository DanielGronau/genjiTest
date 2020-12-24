package org.genji.generators.collection;

import org.assertj.core.api.Assertions;
import org.genji.TypeInfo;
import org.genji.generators.values.StringGenTest;
import org.junit.jupiter.api.Test;

import java.util.*;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

class SetGenTest {

    private static final Random RANDOM = new Random();

    @Test
    @SuppressWarnings({"unchecked", "rawtypes"})
    void generate() {
        List<Set<String>> sets =
            (List) new SetGen()
                       .generate(RANDOM, new TypeInfo(Integer.class, Map.of(), List.of(new TypeInfo(String.class))))
                       .limit(50)
                       .collect(toList());
        for (Set<String> set : sets) {
            assertThat(set.size()).isBetween(0, 20);
            for (String s : set) {
                assertThat(s.length()).isBetween(0, 20);
                for (char c : s.toCharArray())
                    Assertions.assertThat(StringGenTest.DEFAULT_CHARS.indexOf("" + c)).isNotEqualTo(-1);
            }
        }
    }

    @Test
    @SuppressWarnings({"unchecked", "rawtypes"})
    void generate_correctSetType() {
        List<Set<String>> sets =
            (List) new SetGen(TreeSet.class)
                       .generate(RANDOM, new TypeInfo(List.class, Map.of(), List.of(new TypeInfo(String.class))))
                       .limit(2)
                       .collect(toList());
        for (Set<String> set : sets) {
            assertThat(set).isInstanceOf(TreeSet.class);
        }
    }

    @Test
    @Size(from = 3, to = 5)
    @SuppressWarnings({"unchecked", "rawtypes"})
    void generate_sized() throws Exception {
        var size = SetGenTest.class
                       .getDeclaredMethod("generate_sized")
                       .getAnnotation(Size.class);
        List<Set<String>> sets =
            (List) new SetGen()
                       .generate(RANDOM, new TypeInfo(List.class, Map.of(Size.class, size), List.of(new TypeInfo(String.class))))
                       .limit(50)
                       .collect(toList());
        for (Set<String> set : sets) {
            assertThat(set).hasSizeBetween(3, 5);
            for (String s : set) {
                assertThat(s.length()).isBetween(0, 20);
                for (char c : s.toCharArray())
                    assertThat(StringGenTest.DEFAULT_CHARS.indexOf("" + c)).isNotEqualTo(-1);
            }
        }
    }

    @Test
    @Size(from = 3, to = 5)
    @SuppressWarnings({"unchecked", "rawtypes"})
    void generate_nested() throws Exception {
        var size = SetGenTest.class
                       .getDeclaredMethod("generate_nested")
                       .getAnnotation(Size.class);
        List<Set<Set<String>>> setss =
            (List) new SetGen()
                       .generate(RANDOM, new TypeInfo(List.class, Map.of(), List.of(new TypeInfo(Set.class, Map.of(Size.class, size), List.of(new TypeInfo(String.class))))))
                       .limit(5)
                       .collect(toList());
        for (Set<Set<String>> sets : setss) {
            assertThat(sets).hasSizeBetween(0, 20);
            for (Set<String> set : sets) {
                assertThat(set).hasSizeBetween(3, 5);
                for (String s : set) {
                    assertThat(s.length()).isBetween(0, 20);
                    for (char c : s.toCharArray())
                        assertThat(StringGenTest.DEFAULT_CHARS.indexOf("" + c)).isNotEqualTo(-1);
                }
            }
        }
    }

}