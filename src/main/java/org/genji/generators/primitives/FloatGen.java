package org.genji.generators.primitives;

import org.genji.Generator;
import org.genji.TypeInfo;

import java.util.Random;
import java.util.stream.Stream;

import static org.genji.provider.ReflectionSupport.findAnnotation;

@FloatSpec
public class FloatGen implements Generator<Float> {

    public static final FloatGen INSTANCE = new FloatGen();

    private FloatGen() {
    }

    @Override
    public Stream<Float> generate(Random random, TypeInfo typeInfo) {
        FloatSpec floatSpec = findAnnotation(FloatSpec.class, typeInfo, FloatGen.class);
        float from = floatSpec.from();
        float to = floatSpec.to();
        float[] oneOf = floatSpec.oneOf();
        return oneOf.length == 0
                   ? random.doubles(from, to).mapToObj(v -> (float)v)
                   : random.ints(0, oneOf.length).mapToObj(i -> oneOf[i]);
    }
}
