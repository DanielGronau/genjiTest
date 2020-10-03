package org.genji.defaultgenerators;

import org.genji.Generator;
import org.genji.annotations.ByteSpec;
import org.genji.annotations.IntSpec;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import static org.genji.Support.findAnnotation;

@ByteSpec
public class ByteGen implements Generator<Byte> {

    public static final ByteGen INSTANCE = new ByteGen();

    private ByteGen() {
    }

    @Override
    public Stream<Byte> generate(Random random, List<Annotation> annotations, Type... types) {
        ByteSpec byteSpec = findAnnotation(ByteSpec.class, annotations, ByteGen.class);
        byte from = byteSpec.from();
        byte to = byteSpec.to();
        byte[] oneOf = byteSpec.oneOf();
        return oneOf.length == 0
                   ? random.ints(from, to).mapToObj(i -> (byte)i)
                   : random.ints(0, oneOf.length).mapToObj(i -> oneOf[i]);
    }
}
