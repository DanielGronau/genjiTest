package org.genji.generators.collection;

import org.genji.Generator;
import org.genji.TypeInfo;
import org.genji.provider.GeneratorResolver;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.stream.Stream;

import static org.genji.provider.ReflectionSupport.findAnnotation;

@Size
public class SetGen implements Generator<Set<?>> {

    private final Class<?> setClass;

    public SetGen() {
        this(HashSet.class);
    }

    public SetGen(Class<?> setClass) {
        this.setClass = setClass;
    }

    @Override
    public Stream<Set<?>> generate(Random random, TypeInfo typeInfo) {
        Size size = findAnnotation(Size.class, typeInfo, SetGen.class);
        int sizeFrom = Math.max(size.from(), 0);
        int sizeTo = Math.max(size.to(), sizeFrom);

        var typeParameter = typeInfo.getParameterType(0);
        return Stream.generate(
            () -> GeneratorResolver.generatorFor(typeParameter.getType())
                                   .generate(random, typeParameter)
                                   .limit(random.nextInt(sizeTo - sizeFrom) + (long) sizeFrom)
                                   .reduce(getSet(), SetGen::add, (x, y) -> x));
    }

    private Set<?> getSet() {
        try {
            Class<?> setType = setClass.isInterface() ? HashSet.class : setClass;
            return (Set<?>) setType.getConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
            throw new IllegalArgumentException("Set type " + setClass + " has no default constructor", e);
        }
    }

    @SuppressWarnings("unchecked")
    private static Set<?> add(Set<?> set, Object value) {
        ((Set<Object>) set).add(value);
        return set;
    }
}
