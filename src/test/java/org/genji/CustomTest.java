package org.genji;

import org.genji.annotations.Custom;
import org.genji.annotations.GenjiTest;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Custom(target = Integer.class, generator = CustomTest.FortyTwo.class)
@Custom(target = String.class, generator = CustomTest.Foo.class)
public class CustomTest {

    @GenjiTest
    void classVsParameterLevel(
        int a,
        @Custom(target = Integer.class, generator = CustomTest.TwentyThree.class) Integer b,
        String c) {
        assertEquals(42, a);
        assertEquals(23, b);
        assertEquals("foo", c);
    }

    @GenjiTest
    @Custom(target = Integer.class, generator = CustomTest.TwentyThree.class)
    @Custom(target = String.class, generator = CustomTest.Bar.class)
    void methodVsParameterLevel(
        int a,
        @Custom(target = Integer.class, generator = CustomTest.Nineteen.class) Integer b,
        @Custom(target = Integer.class, generator = CustomTest.Nineteen.class) String c,
        @Custom(target = Integer.class, generator = CustomTest.FortyTwo.class)
        @Custom(target = String.class, generator = CustomTest.Foo.class) String d
    ) {
        assertEquals(23, a);
        assertEquals(19, b);
        assertEquals("bar", c);
        assertEquals("foo", d);
    }

    public static class FortyTwo implements Generator<Integer> {

        @Override
        public Stream<Integer> generate(Random random, List<Annotation> annotations, Type... parameterTypes) {
            return Stream.generate(() -> 42);
        }
    }

    public static class TwentyThree implements Generator<Integer> {

        @Override
        public Stream<Integer> generate(Random random, List<Annotation> annotations, Type... parameterTypes) {
            return Stream.generate(() -> 23);
        }
    }

    public static class Nineteen implements Generator<Integer> {

        @Override
        public Stream<Integer> generate(Random random, List<Annotation> annotations, Type... parameterTypes) {
            return Stream.generate(() -> 19);
        }
    }

    public static class Foo implements Generator<String> {

        @Override
        public Stream<String> generate(Random random, List<Annotation> annotations, Type... parameterTypes) {
            return Stream.generate(() -> "foo");
        }
    }

    public static class Bar implements Generator<String> {

        @Override
        public Stream<String> generate(Random random, List<Annotation> annotations, Type... parameterTypes) {
            return Stream.generate(() -> "bar");
        }
    }

}
