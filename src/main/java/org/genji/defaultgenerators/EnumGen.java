package org.genji.defaultgenerators;

import org.genji.Generator;
import org.genji.NoGeneratorFoundException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

public class EnumGen<E extends Enum<E>> implements Generator<E> {

    private final Class<E> enumClass;

    public EnumGen(Class<E> enumClass) {
        this.enumClass = enumClass;
        if (enumClass.getEnumConstants().length == 0) {
            throw new NoGeneratorFoundException("Can't generator for empty enum " + enumClass.getSimpleName());
        }
    }

    @Override
    public Stream<E> generate(Random random, List<Annotation> annotations, Type... parameterTypes) {
        var enumConstants = enumClass.getEnumConstants();
        return random.ints(0, enumConstants.length).mapToObj(i -> enumConstants[i]);
    }
}
