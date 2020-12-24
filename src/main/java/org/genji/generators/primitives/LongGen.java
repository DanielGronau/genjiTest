package org.genji.generators.primitives;

import org.genji.Generator;
import org.genji.TypeInfo;

import java.util.Random;
import java.util.stream.Stream;

import static org.genji.provider.ReflectionSupport.findAnnotation;

@LongSpec
public class LongGen implements Generator<Long> {

    public static final LongGen INSTANCE = new LongGen();

    private LongGen() {
    }

    @Override
    public Stream<Long> generate(Random random, TypeInfo typeInfo) {
        LongSpec longSpec = findAnnotation(LongSpec.class, typeInfo, LongGen.class);
        long from = longSpec.from();
        long to = longSpec.to();
        long[] oneOf = longSpec.oneOf();
        return oneOf.length == 0
                   ? random.longs(from, to).boxed()
                   : random.ints(0, oneOf.length).mapToObj(i -> oneOf[i]);
    }
}
