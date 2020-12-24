package org.genji.generators.primitives;

import org.genji.Generator;
import org.genji.TypeInfo;

import java.util.Random;
import java.util.stream.Stream;

import static org.genji.provider.ReflectionSupport.findAnnotation;

@IntSpec
public class IntGen implements Generator<Integer> {

    public static final IntGen INSTANCE = new IntGen();

    private IntGen() {
    }

    @Override
    public Stream<Integer> generate(Random random, TypeInfo typeInfo) {
        IntSpec intSpec = findAnnotation(IntSpec.class, typeInfo, IntGen.class);
        int from = intSpec.from();
        int to = intSpec.to();
        int[] oneOf = intSpec.oneOf();
        return oneOf.length == 0
                   ? random.ints(from, to).boxed()
                   : random.ints(0, oneOf.length).mapToObj(i -> oneOf[i]);
    }
}
