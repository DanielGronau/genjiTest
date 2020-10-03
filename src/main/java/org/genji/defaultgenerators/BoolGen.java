package org.genji.defaultgenerators;

import org.genji.Generator;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

public class BoolGen implements Generator<Boolean> {

    public static final BoolGen INSTANCE = new BoolGen();

    private BoolGen() {
    }

    @Override
    public Stream<Boolean> generate(Random random, List<Annotation> annotations, Type... parameterTypes) {
        return Stream.generate(random::nextBoolean);
    }
}
