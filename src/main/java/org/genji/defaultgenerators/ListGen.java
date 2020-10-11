package org.genji.defaultgenerators;

import org.genji.Generator;
import org.genji.GeneratorResolver;
import org.genji.ReflectionSupport;
import org.genji.annotations.Size;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import static org.genji.ReflectionSupport.findAnnotation;

@Size
public class ListGen implements Generator<List<?>> {


    private final Class<?> listClass;

    public ListGen() {
        this(ArrayList.class);
    }

    public ListGen(Class<?> listClass) {
        this.listClass = listClass;
    }

    @Override
    public Stream<List<?>> generate(Random random, List<Annotation> annotations, Type... types) {
        Size size = findAnnotation(Size.class, annotations, ListGen.class);
        int sizeFrom = Math.max(size.from(), 0);
        int sizeTo = Math.max(size.to(), sizeFrom);

        return Stream.generate(
            () -> GeneratorResolver.generatorFor(types[0])
                                   .generate(random, annotations, ReflectionSupport.getParameterTypes(types[0]))
                                   .limit(random.nextInt(sizeTo - sizeFrom) + sizeFrom)
                                   .reduce(getList(), ListGen::add, (x, y) -> x));
    }

    private List<?> getList() {
        try {
            Class<?> listType = listClass.isInterface() ? ArrayList.class : listClass;
            return (List<?>) listType.getConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    private static List<?> add(List<?> list, Object value) {
        ((List<Object>) list).add(value);
        return list;
    }
}
