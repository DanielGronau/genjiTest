package org.genji.defaultgenerators;

import org.genji.annotations.Size;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;

import static java.util.stream.Collectors.toList;
import static org.genji.Utils.assertBetween;
import static org.genji.Utils.getParametrizedType;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ListGenTest {

    private static final Random RANDOM = new Random();

    @Test
    @SuppressWarnings({"unchecked", "rawtypes"})
    void generate() {
        List<List<String>> lists =
            (List) new ListGen()
                       .generate(RANDOM, List.of(), String.class)
                       .limit(50)
                       .collect(toList());
        for (List<String> list : lists) {
            assertBetween(list.size(), 0, 20);
            for (String s : list) {
                assertBetween(s.length(), 0, 20);
                for (char c : s.toCharArray())
                    assertTrue(StringGenTest.DEFAULT_CHARS.contains("" + c));
            }
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
                       .generate(RANDOM, List.of(size), String.class)
                       .limit(50)
                       .collect(toList());
        for (List<String> list : lists) {
            assertBetween(list.size(), 3, 5);
            for (String s : list) {
                assertBetween(s.length(), 0, 20);
                for (char c : s.toCharArray())
                    assertTrue(StringGenTest.DEFAULT_CHARS.contains("" + c));
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
                       .generate(RANDOM, List.of(size), getParametrizedType(List.class, String.class))
                       .limit(5)
                       .collect(toList());
        for (List<List<String>> lists : listss) {
            assertBetween(lists.size(), 3, 5);
            for (List<String> list : lists) {
                assertBetween(list.size(), 3, 5);
                for (String s : list) {
                    assertBetween(s.length(), 0, 20);
                    for (char c : s.toCharArray())
                        assertTrue(StringGenTest.DEFAULT_CHARS.contains("" + c));
                }
            }
        }
    }

}