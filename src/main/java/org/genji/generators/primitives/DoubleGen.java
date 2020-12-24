package org.genji.generators.primitives;

import org.genji.Generator;
import org.genji.TypeInfo;

import java.util.Random;
import java.util.stream.Stream;

import static org.genji.provider.ReflectionSupport.findAnnotation;

@DoubleSpec
public class DoubleGen implements Generator<Double> {

    public static final DoubleGen INSTANCE = new DoubleGen();

    private DoubleGen() {
    }

    @Override
    public Stream<Double> generate(Random random, TypeInfo typeInfo) {
        DoubleSpec doubleSpec = findAnnotation(DoubleSpec.class, typeInfo, DoubleGen.class);
        double from = doubleSpec.from();
        double to = doubleSpec.to();
        double[] oneOf = doubleSpec.oneOf();
        return oneOf.length == 0
                   ? random.doubles(from, to).boxed()
                   : random.ints(0, oneOf.length).mapToObj(i -> oneOf[i]);
    }
}
