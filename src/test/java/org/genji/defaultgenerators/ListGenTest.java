package org.genji.defaultgenerators;

import org.genji.TypeInfo;
import org.genji.annotations.Size;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

class ListGenTest {

    private static final Random RANDOM = new Random();

    @Test
    @SuppressWarnings({"unchecked", "rawtypes"})
    void generate() {
        List<List<String>> lists =
            (List) new ListGen()
                       .generate(RANDOM, new TypeInfo(Integer.class, Map.of(), List.of(new TypeInfo(String.class))))
                       .limit(50)
                       .collect(toList());
        for (List<String> list : lists) {
            assertThat(list.size()).isBetween(0, 20);
            for (String s : list) {
                assertThat(s.length()).isBetween(0, 20);
                for (char c : s.toCharArray())
                    assertThat(StringGenTest.DEFAULT_CHARS.indexOf("" + c)).isNotEqualTo(-1);
            }
        }
    }

    @Test
    @SuppressWarnings({"unchecked", "rawtypes"})
    void generate_correctListType() {
        List<List<String>> lists =
            (List) new ListGen(LinkedList.class)
                       .generate(RANDOM, new TypeInfo(List.class, Map.of(), List.of(new TypeInfo(String.class))))
                       .limit(2)
                       .collect(toList());
        for (List<String> list : lists) {
            assertThat(list).isInstanceOf(LinkedList.class);
        }
    }

    @Test
    @Size(from = 3, to = 5)
    @SuppressWarnings({"unchecked", "rawtypes"})
    void generate_sized() throws Exception {
        var size = ListGenTest.class
                       .getDeclaredMethod("generate_sized")
                       .getAnnotation(Size.class);
        List<List<String>> lists =
            (List) new ListGen()
                       .generate(RANDOM, new TypeInfo(List.class, Map.of(Size.class, size), List.of(new TypeInfo(String.class))))
                       .limit(50)
                       .collect(toList());
        for (List<String> list : lists) {
            assertThat(list).hasSizeBetween(3, 5);
            for (String s : list) {
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
        var size = ListGenTest.class
                       .getDeclaredMethod("generate_nested")
                       .getAnnotation(Size.class);
        List<List<List<String>>> listss =
            (List) new ListGen()
                       .generate(RANDOM, new TypeInfo(List.class, Map.of(), List.of(new TypeInfo(List.class, Map.of(Size.class, size), List.of(new TypeInfo(String.class))))))
                       .limit(5)
                       .collect(toList());
        for (List<List<String>> lists : listss) {
            assertThat(lists).hasSizeBetween(0, 20);
            for (List<String> list : lists) {
                assertThat(list).hasSizeBetween(3, 5);
                for (String s : list) {
                    assertThat(s.length()).isBetween(0, 20);
                    for (char c : s.toCharArray())
                        assertThat(StringGenTest.DEFAULT_CHARS.indexOf("" + c)).isNotEqualTo(-1);
                }
            }
        }
    }

}