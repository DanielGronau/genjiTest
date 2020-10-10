package org.genji;

import org.genji.annotations.GenjiTest;
import org.genji.annotations.Seed;
import org.genji.annotations.Size;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FooTest {

    @GenjiTest
    void appendRule(@Seed(25) String s1, String s2) {
        var appended = s1 + s2;
        assertEquals(appended.length(), s1.length() + s2.length());
    }

    @GenjiTest
    void addRule(int i1, Integer i2) {
        assertEquals(i1 + i2, i2 + i1);
    }

    @GenjiTest
    void reverseRule(List<@Size(from = 1, to = 3) LinkedList<String>> list) {
        List<List<String>> original = List.copyOf(list);
        Collections.reverse(list);
        Collections.reverse(list);
        assertEquals(list, original);
    }

}
