package org.genji;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;

final public class ReflectionSupport {

    private ReflectionSupport() {
    }

    public static <T extends Annotation> Optional<T> findAnnotation(
        Class<T> annotationClass,
        Collection<? extends Annotation> annotations) {
        return annotations.stream()
                          .filter(annotationClass::isInstance)
                          .map(annotationClass::cast)
                          .findFirst();
    }

    // attention, returns null if no annotation is found
    public static <T extends Annotation> T findAnnotation(
        Class<T> annotationClass,
        Collection<? extends Annotation> annotations,
        Class<?> self) {
        return findAnnotation(annotationClass, annotations)
                          .orElseGet(() -> self.getAnnotation(annotationClass));
    }


    public static Type[] getParameterTypes(Type type) {
        if (type instanceof ParameterizedType) {
            var parameterizedType = (ParameterizedType) type;
            return parameterizedType.getActualTypeArguments();
        }
        return new Type[]{};
    }

    public static Class<?> getRawType(Type type) {
        return boxIfNecessary((Class<?>) (type instanceof ParameterizedType
                        ? ((ParameterizedType) type).getRawType()
                        : type));
    }

    public static <T> Optional<T> construct(Class<T> targetClass, Object ... arguments) {
        try {
            Class<?>[] parameters = Arrays.stream(arguments).map(Object::getClass).toArray(Class<?>[]::new);
            return Optional.of(targetClass.getConstructor(parameters))
                           .map(c -> {
                               try {
                                   return c.newInstance(arguments);
                               } catch (ReflectiveOperationException e) {
                                   throw new RuntimeException(e);
                               }
                           });
        } catch (NoSuchMethodException e) {
            return Optional.empty();
        }
    }

    public static Iterable<Class<?>> superTypes(Class<?> targetClass) {
        List<Class<?>> todo = new ArrayList<>();
        todo.add(boxIfNecessary(targetClass));
        return () -> new Iterator<>() {

            @Override
            public boolean hasNext() {
                return ! todo.isEmpty();
            }

            @Override
            public Class<?> next() {
                if (! hasNext()) {
                    throw new NoSuchElementException();
                }
                Class<?> superClassOrInterface = todo.remove(0);
                if (superClassOrInterface.getSuperclass() != null) {
                    todo.add(superClassOrInterface.getSuperclass());
                }
                todo.addAll(Arrays.asList(superClassOrInterface.getInterfaces()));
                return superClassOrInterface;
            }
        };
    }

    // returns boxed class when called for a primitive typo
    public static Class<?> boxIfNecessary(Class<?> targetClass) {
        if (targetClass.isPrimitive()) {
            // hack, there is no way to do this in JDK directly other than listing all translations
            return Array.get(Array.newInstance(targetClass, 1), 0).getClass();
        }
        return targetClass;
    }

}
