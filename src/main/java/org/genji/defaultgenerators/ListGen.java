package org.genji.defaultgenerators;

import org.genji.Generator;
import org.genji.Support;
import org.genji.annotations.Size;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.genji.Support.findAnnotation;

@Size
public class ListGen implements Generator<List<?>> {

    public static final ListGen INSTANCE = new ListGen();

    private ListGen() {
    }

    @Override
    public Stream<List<?>> generate(Random random, List<Annotation> annotations, Type... types) {
        Size size = findAnnotation(Size.class, annotations, ListGen.class);
        int sizeFrom = Math.max(size.from(), 0);
        int sizeTo = Math.max(size.to(), sizeFrom);

        return Stream.generate(
            () -> Support.generatorFor(types[0])
                         .generate(random, annotations, Support.getParameterTypes(types[0]))
                         .limit(random.nextInt(sizeTo - sizeFrom) + sizeFrom)
                         .collect(Collectors.toList()));
    }
}
