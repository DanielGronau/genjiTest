package org.genji.defaultgenerators;

import org.genji.Generator;
import org.genji.annotations.LongSpec;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import static org.genji.ReflectionSupport.findAnnotation;

@LongSpec
public class LongGen implements Generator<Long> {

    public static final LongGen INSTANCE = new LongGen();

    private LongGen() {
    }

    @Override
    public Stream<Long> generate(Random random, List<Annotation> annotations, Type... types) {
        LongSpec longSpec = findAnnotation(LongSpec.class, annotations, LongGen.class);
        long from = longSpec.from();
        long to = longSpec.to();
        long[] oneOf = longSpec.oneOf();
        return oneOf.length == 0
                   ? random.longs(from, to).boxed()
                   : random.ints(0, oneOf.length).mapToObj(i -> oneOf[i]);
    }
}
