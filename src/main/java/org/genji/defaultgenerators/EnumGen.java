package org.genji.defaultgenerators;

import org.genji.Generator;
import org.genji.TypeInfo;

import java.util.Random;
import java.util.stream.Stream;

public class EnumGen<E extends Enum<E>> implements Generator<E> {

    @Override
    public Stream<E> generate(Random random, TypeInfo typeInfo) {
        var enumConstants = (E[]) typeInfo.getType().getEnumConstants();
        return random.ints(0, enumConstants.length).mapToObj(i -> enumConstants[i]);
    }
}
