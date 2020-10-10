package org.genji.defaultgenerators;

import org.genji.Generator;
import org.genji.annotations.IntSpec;
import org.genji.annotations.ShortSpec;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import static org.genji.Support.findAnnotation;

@ShortSpec
public class ShortGen implements Generator<Short> {

    public static final ShortGen INSTANCE = new ShortGen();

    private ShortGen() {
    }

    @Override
    public Stream<Short> generate(Random random, List<Annotation> annotations, Type... types) {
        ShortSpec shortSpec = findAnnotation(ShortSpec.class, annotations, ShortGen.class);
        short from = shortSpec.from();
        short to = shortSpec.to();
        short[] oneOf = shortSpec.oneOf();
        return oneOf.length == 0
                   ? random.ints(from, to).mapToObj(i -> (short)i)
                   : random.ints(0, oneOf.length).mapToObj(i -> oneOf[i]);
    }
}