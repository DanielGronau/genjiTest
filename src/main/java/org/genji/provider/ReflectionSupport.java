package org.genji.provider;

import org.genji.TypeInfo;

import java.lang.annotation.Annotation;
import java.lang.annotation.Repeatable;
import java.lang.reflect.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

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

    public static <T extends Annotation> T findAnnotation(
        Class<T> annotationClass,
        TypeInfo typeInfo,
        Class<?> self) {
        return Optional.ofNullable(typeInfo.getAnnotations().get(annotationClass))
                       .map(annotationClass::cast)
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

    public static <T> Optional<T> construct(Class<T> targetClass, Object... arguments) {
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
                return !todo.isEmpty();
            }

            @Override
            public Class<?> next() {
                if (!hasNext()) {
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

    public static Map<Class<? extends Annotation>, Annotation> methodAnnotations(Method method) {
        var targetClass = method.getDeclaringClass();
        var targetPackage = targetClass.getPackage();
        Map<Class<? extends Annotation>, Annotation> defaultMap =
            Arrays.stream(targetPackage.getAnnotations())
                  .collect(toMap(Annotation::getClass, a -> a, (a1, b) -> b));
        defaultMap.putAll(
            Arrays.stream(targetClass.getAnnotations())
                  .collect(toMap(Annotation::getClass, a -> a, (a1, b) -> b)));
        defaultMap.putAll(
            Arrays.stream(method.getAnnotations())
                  .collect(toMap(Annotation::getClass, a -> a, (a1, b) -> b)));
        return defaultMap;
    }

    public static List<Map<Class<? extends Annotation>, Annotation>> parameterAnnotations(Method method) {
        var defaultMap = methodAnnotations(method);
        return Arrays.stream(method.getParameterAnnotations())
                     .map(annotations -> Arrays.stream(annotations).collect(
                         toMap(Annotation::getClass, a -> a, (a1, b) -> b, () -> new HashMap<>(defaultMap))))
                     .collect(toList());
    }

    public static <A extends Annotation> List<A> repeatableAnnotationOfType(AnnotatedElement element, Class<A> type) {
        return Arrays.asList(element.getAnnotationsByType(type));
    }


    //package before class before method
    public static <A extends Annotation> List<A> repeatableMethodAnnotation(Method targetMethod, Class<A> type) {
        List<A> result = new ArrayList<>();
        result.addAll(repeatableAnnotationOfType(targetMethod.getDeclaringClass().getPackage(), type));
        result.addAll(repeatableAnnotationOfType(targetMethod.getDeclaringClass(), type));
        result.addAll(repeatableAnnotationOfType(targetMethod, type));
        return result;
    }

    public static <A extends Annotation> Optional<A> methodAnnotation(Method method, Class<A> type) {
        return annotationOfType(method, type)
                   .or(() -> annotationOfType(method.getDeclaringClass(), type))
                   .or(() -> annotationOfType(method.getDeclaringClass().getPackage(), type));
    }

    public static <A extends Annotation> Optional<A> annotationOfType(AnnotatedElement element, Class<A> type) {
        return Arrays.stream(element.getAnnotationsByType(type)).findFirst();
    }

    public static <A extends Annotation> List<A> filterRepeatableAnnotation(List<Annotation> annotations, Class<A> type) {
        var single = annotations.stream().filter(type::isInstance).map(type::cast).collect(toList());
        if (!single.isEmpty()) {
            return single;
        }
        var annotationsByType = type.getAnnotationsByType(Repeatable.class);
        if (annotationsByType.length == 1) {
            var wrapperType = annotationsByType[0].value();
            return annotations
                       .stream()
                       .filter(wrapperType::isInstance)
                       .map(wrapperType::cast)
                       .flatMap(ts -> {
                               try {
                                   return Arrays.stream((A[]) (wrapperType.getMethod("value").invoke(ts)));
                               } catch (ReflectiveOperationException e) {
                                   throw new RuntimeException(e);
                               }
                           }
                       ).collect(toList());
        }
        return List.of();
    }

    public static List<TypeInfo> typeInfos(Method method) {
        var maps = parameterAnnotations(method);
        var apts = method.getAnnotatedParameterTypes();
        return IntStream.range(0, maps.size())
                        .mapToObj(i -> new TypeInfo(method.getParameterTypes()[i],
                                maps.get(i),
                                annotatedActualTypeArguments(apts[i])
                                      .map(annotatedType -> typeInfo(annotatedType, maps.get(i))).collect(toList())

                            )
                        )
                        .collect(Collectors.toList());
    }

    public static TypeInfo typeInfo(AnnotatedType type, Map<Class<? extends Annotation>, Annotation> parentAnnotations) {
        var parentCopy = new HashMap<>(parentAnnotations);
        var annotations = Arrays.stream(type.getAnnotations())
                                .reduce(parentCopy,
                                    (map, annotation) -> {
                                        map.put(annotation.getClass(), annotation);
                                        return map;
                                    }, (x, y) -> x);
        return new TypeInfo(typeToClass(type.getType()), annotations,
            annotatedActualTypeArguments(type).map(annotatedType -> typeInfo(annotatedType, annotations)).collect(toList())
        );
    }

    private static Stream<AnnotatedType> annotatedActualTypeArguments(AnnotatedType type) {
        if(type instanceof AnnotatedParameterizedType) {
            return Arrays.stream(((AnnotatedParameterizedType) type).getAnnotatedActualTypeArguments());
        }
        return Stream.empty();
    }

    public static Class<?> typeToClass(Type type) {
        if (type instanceof Class) {
            return (Class<?>) type;
        }
        if (type instanceof ParameterizedType) {
            return typeToClass(((ParameterizedType) type).getRawType());
        }
        throw new IllegalArgumentException();
    }

}
