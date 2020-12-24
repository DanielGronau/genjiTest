package org.genji.generators.primitives;

import org.genji.Generator;
import org.genji.TypeInfo;

import java.util.Random;
import java.util.stream.Stream;

import static org.genji.provider.ReflectionSupport.findAnnotation;

@ShortSpec
public class ShortGen implements Generator<Short> {

    public static final ShortGen INSTANCE = new ShortGen();

    private ShortGen() {
    }

    @Override
    public Stream<Short> generate(Random random, TypeInfo typeInfo) {
        ShortSpec shortSpec = findAnnotation(ShortSpec.class, typeInfo, ShortGen.class);
        short from = shortSpec.from();
        short to = shortSpec.to();
        short[] oneOf = shortSpec.oneOf();
        return oneOf.length == 0
                   ? random.ints(from, to).mapToObj(i -> (short)i)
                   : random.ints(0, oneOf.length).mapToObj(i -> oneOf[i]);
    }
}
