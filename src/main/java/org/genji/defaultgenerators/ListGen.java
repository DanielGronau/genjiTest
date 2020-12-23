package org.genji.defaultgenerators;

import org.genji.Generator;
import org.genji.GeneratorResolver;
import org.genji.TypeInfo;
import org.genji.annotations.Size;

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
    public Stream<List<?>> generate(Random random, TypeInfo typeInfo) {
        Size size = findAnnotation(Size.class, typeInfo, ListGen.class);
        int sizeFrom = Math.max(size.from(), 0);
        int sizeTo = Math.max(size.to(), sizeFrom);

        var typeParameter = typeInfo.getParameterType(0);
        return Stream.generate(
            () -> GeneratorResolver.generatorFor(typeParameter.getType())
                                   .generate(random, typeParameter)
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
