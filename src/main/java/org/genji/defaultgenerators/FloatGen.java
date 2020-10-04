package org.genji.defaultgenerators;

import org.genji.Generator;
import org.genji.annotations.DoubleSpec;
import org.genji.annotations.FloatSpec;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import static org.genji.Support.findAnnotation;

@FloatSpec
public class FloatGen implements Generator<Float> {

    public static final FloatGen INSTANCE = new FloatGen();

    private FloatGen() {
    }

    @Override
    public Stream<Float> generate(Random random, List<Annotation> annotations, Type... types) {
        FloatSpec floatSpec = findAnnotation(FloatSpec.class, annotations, FloatGen.class);
        float from = floatSpec.from();
        float to = floatSpec.to();
        float[] oneOf = floatSpec.oneOf();
        return oneOf.length == 0
                   ? random.doubles(from, to).mapToObj(v -> (float)v)
                   : random.ints(0, oneOf.length).mapToObj(i -> oneOf[i]);
    }
}
