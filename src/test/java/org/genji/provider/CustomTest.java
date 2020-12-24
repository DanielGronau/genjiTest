package org.genji.provider;

import org.genji.Generator;
import org.genji.GenjiTest;
import org.genji.TypeInfo;
import org.genji.annotations.Custom;

import java.util.Random;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@Custom(target = Integer.class, generator = CustomTest.FortyTwo.class)
@Custom(target = String.class, generator = CustomTest.MyStringGen.class, arguments = "foo")
public class CustomTest {

    @GenjiTest
    void classVsParameterLevel(
        int a,
        @Custom(target = Integer.class, generator = CustomTest.TwentyThree.class) Integer b,
        String c) {
        assertThat(a).isEqualTo(42);
        assertThat(b).isEqualTo(23);
        assertThat(c).isEqualTo("foo");
    }

    @GenjiTest
    @Custom(target = Integer.class, generator = CustomTest.TwentyThree.class)
    @Custom(target = String.class, generator = CustomTest.MyStringGen.class, arguments = "bar")
    void methodVsParameterLevel(
        int a,
        @Custom(target = Integer.class, generator = CustomTest.Nineteen.class) Integer b,
        @Custom(target = Integer.class, generator = CustomTest.Nineteen.class) String c,
        @Custom(target = Integer.class, generator = CustomTest.FortyTwo.class)
        @Custom(target = String.class, generator = CustomTest.MyStringGen.class, arguments = "foo") String d
    ) {
        assertThat(a).isEqualTo(23);
        assertThat(b).isEqualTo(19);
        assertThat(c).isEqualTo("bar");
        assertThat(d).isEqualTo("foo");
    }

    public static class FortyTwo implements Generator<Integer> {

        @Override
        public Stream<Integer> generate(Random random, TypeInfo typeInfo) {
            return Stream.generate(() -> 42);
        }
    }

    public static class TwentyThree implements Generator<Integer> {

        @Override
        public Stream<Integer> generate(Random random, TypeInfo typeInfo) {
            return Stream.generate(() -> 23);
        }
    }

    public static class Nineteen implements Generator<Integer> {

        @Override
        public Stream<Integer> generate(Random random, TypeInfo typeInfo) {
            return Stream.generate(() -> 19);
        }
    }

    public static class MyStringGen implements Generator<String> {

        private final String arg;

        public MyStringGen(String arg) {
            this.arg = arg;
        }

        @Override
        public Stream<String> generate(Random random, TypeInfo typeInfo) {
            return Stream.generate(() -> arg);
        }
    }

}
