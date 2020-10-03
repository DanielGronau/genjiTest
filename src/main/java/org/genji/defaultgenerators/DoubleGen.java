package org.genji.defaultgenerators;

import org.genji.Generator;
import org.genji.annotations.DoubleSpec;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import static org.genji.Support.findAnnotation;

@DoubleSpec
public class DoubleGen implements Generator<Double> {

    public static final DoubleGen INSTANCE = new DoubleGen();

    private DoubleGen() {
    }

    @Override
    public Stream<Double> generate(Random random, List<Annotation> annotations, Type... types) {
        DoubleSpec doubleSpec = findAnnotation(DoubleSpec.class, annotations, DoubleGen.class);
        double from = doubleSpec.from();
        double to = doubleSpec.to();
        double[] oneOf = doubleSpec.oneOf();
        return oneOf.length == 0
                   ? random.doubles(from, to).boxed()
                   : random.ints(0, oneOf.length).mapToObj(i -> oneOf[i]);
    }
}
