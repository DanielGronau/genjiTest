package org.genji.generators.primitives;

import org.genji.Generator;
import org.genji.TypeInfo;

import java.util.Random;
import java.util.stream.Stream;

public class BoolGen implements Generator<Boolean> {

    public static final BoolGen INSTANCE = new BoolGen();

    private BoolGen() {
    }

    @Override
    public Stream<Boolean> generate(Random random, TypeInfo typeInfo) {
        return Stream.generate(random::nextBoolean);
    }
}
