package org.genji.defaultgenerators;

import org.genji.Generator;
import org.genji.annotations.Between;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Stream;

import static org.genji.Support.findAnnotation;

public class IntegerGen implements Generator<Integer> {

    @Override
    public Stream<Integer> generate(Random random, List<Annotation> annotations, Type... types) {
        Optional<Between> sizeAnnotation = findAnnotation(Between.class, annotations);
        int from = sizeAnnotation.map(a -> (int) Math.max(Integer.MIN_VALUE, a.from())).orElse(Integer.MIN_VALUE);
        int to = sizeAnnotation.map(a -> (int) Math.min(from, Math.min(Integer.MAX_VALUE, a.to()))).orElse(Integer.MAX_VALUE);
        return random.ints(from, to - 1).boxed();
    }
}
