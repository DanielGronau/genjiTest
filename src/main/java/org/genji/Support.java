package org.genji;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Optional;

final public class Support {

    public static <T extends Annotation> Optional<T> findAnnotation(
        Class<T> annotationClass,
        Collection<? extends Annotation> annotations) {
        return annotations.stream()
                          .filter(annotationClass::isInstance)
                          .map(annotationClass::cast)
                          .findFirst();
    }

    public static Generator<?> generatorFor(Type type) throws NoGeneratorFoundException {
        var elementClass = (Class<?>) (type instanceof ParameterizedType
                                           ? ((ParameterizedType) type).getRawType()
                                           : type);
        return GeneratorResolver
                   .resolve(elementClass)
                   .orElseThrow(() -> new NoGeneratorFoundException("for type " + type.toString()));
    }

    public static Type[] getParameterTypes(Type type) {
        if (type instanceof ParameterizedType) {
            var parameterizedType = (ParameterizedType) type;
            return parameterizedType.getActualTypeArguments();
        }
        return new Type[]{};
    }

}
