package org.genji.defaultgenerators;

import org.genji.Generator;
import org.genji.GeneratorResolver;
import org.genji.NoGeneratorFoundException;
import org.genji.Support;
import org.genji.annotations.Size;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.genji.Support.findAnnotation;

public class ListGen implements Generator<List<?>> {

    static final int DEFAULT_LENGTH = 20;

    @Override
    public Stream<List<?>> generate(Random random, List<Annotation> annotations, Type... types) {
        Optional<Size> sizeAnnotation = findAnnotation(Size.class, annotations);
        int sizeFrom = sizeAnnotation.map(a -> (int) Math.max(a.from(), 0)).orElse(0);
        int sizeTo = sizeAnnotation.map(a -> (int) Math.max(a.to(), sizeFrom)).orElse(DEFAULT_LENGTH);

        return Stream.generate(
            () -> Support.generatorFor(types[0])
                      .generate(random, annotations, Support.getParameterTypes(types[0]))
                      .limit(random.nextInt(sizeTo - sizeFrom) + sizeFrom)
                      .collect(Collectors.toList()));
    }
}
