package org.genji.provider;

import org.genji.GenjiTest;
import org.genji.annotations.Seed;
import org.junit.jupiter.api.AfterAll;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class SeedTest {

    private static List<String> list1;
    private static List<String> list2;
    private static List<String> list3;
    private static List<String> list4;

    @GenjiTest
    @Seed(42)
    void list1(List<String> list) {
       list1 = list;
    }

    @GenjiTest
    @Seed(42)
    void list2(List<String> list) {
       list2 = list;
    }

    @GenjiTest
    @Seed(43)
    void list3(List<String> list) {
       list3 = list;
    }

    @GenjiTest
    void list4(List<String> list) {
       list4 = list;
    }

    @AfterAll
    static void test() {
        assertThat(list1).isEqualTo(list2);
        assertThat(list1).isNotEqualTo(list3);
        assertThat(list1).isNotEqualTo(list4);
        assertThat(list3).isNotEqualTo(list4);
    }
}
