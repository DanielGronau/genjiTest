package org.genji.generators.primitives;

import org.genji.Generator;
import org.genji.TypeInfo;

import java.util.Random;
import java.util.stream.Stream;

import static org.genji.provider.ReflectionSupport.findAnnotation;

@ByteSpec
public class ByteGen implements Generator<Byte> {

    public static final ByteGen INSTANCE = new ByteGen();

    private ByteGen() {
    }

    @Override
    public Stream<Byte> generate(Random random, TypeInfo typeInfo) {
        ByteSpec byteSpec = findAnnotation(ByteSpec.class, typeInfo, ByteGen.class);
        byte from = byteSpec.from();
        byte to = byteSpec.to();
        byte[] oneOf = byteSpec.oneOf();
        return oneOf.length == 0
                   ? random.ints(from, to).mapToObj(i -> (byte)i)
                   : random.ints(0, oneOf.length).mapToObj(i -> oneOf[i]);
    }
}
