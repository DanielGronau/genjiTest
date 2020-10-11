package org.genji;

import org.genji.annotations.*;
import org.genji.defaultgenerators.WithNullGen;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static org.genji.ReflectionSupport.findAnnotation;
import static org.genji.ReflectionSupport.getParameterTypes;

public class GenjiProvider implements ArgumentsProvider {

    private static final int DEFAULT_SAMPLE_SIZE = 20;
    private static final Random RANDOM = new Random();

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
        var annotations = collectAnnotations(extensionContext);
        var sampleSize = getSampleSize(extensionContext);
        var generators = getGenerators(extensionContext);
        var iterators =
            IntStream.range(0, generators.size())
                     .mapToObj(i -> generators
                                        .get(i)
                                        .generate(
                                            getRandom(annotations.get(i)),
                                            List.copyOf(annotations.get(i).values()),
                                            getParameterTypes(extensionContext
                                                                  .getRequiredTestMethod()
                                                                  .getParameters()[i]
                                                                  .getParameterizedType()))
                                        .limit(sampleSize)
                                        .iterator()).collect(Collectors.toList());
        return IntStream.range(0, sampleSize)
                        .mapToObj(i -> Arguments.of(iterators.stream().map(Iterator::next).toArray()));
    }

    private static Random getRandom(Map<Class<? extends Annotation>, Annotation> annotationMap) {
        return Optional.ofNullable(annotationMap.get(Seed.class))
                       .map(Seed.class::cast)
                       .map(Seed::value)
                       .map(Random::new)
                       .orElse(RANDOM);
    }

    private static List<? extends Generator<?>> getGenerators(ExtensionContext extensionContext) {
        var customGenerators = collectCustomGenerators(extensionContext);
        Method method = extensionContext.getRequiredTestMethod();
        List<Generator<?>> generators =
            IntStream.range(0, method.getParameterCount())
                .mapToObj(i -> {
                    Parameter parameter = method.getParameters()[i];
                    Map<Class<?>, Class<?>> customMap = customGenerators.get(i);
                    return GeneratorResolver.resolve(parameter.getType(), customMap).orElseThrow(
                        () -> new NoGeneratorFoundException("for " + parameter));
                })
                .collect(toList());
        for (int i = 0; i < generators.size(); i++) {
            var index = i;
            findAnnotation(WithNulls.class, List.of(method.getParameterAnnotations()[i]))
                .ifPresent(withNulls -> generators.set(index,
                    WithNullGen.withNulls(withNulls.probability(), generators.get(index))
                ));
        }
        return generators;
    }

    private static Integer getSampleSize(ExtensionContext extensionContext) {
        return Optional.ofNullable(extensionContext.getRequiredTestMethod().getAnnotation(Samples.class))
                       .or(() -> Optional.ofNullable(extensionContext.getRequiredTestClass().getAnnotation(Samples.class)))
                       .map(Samples::value)
                       .filter(v -> v > 0)
                       .orElse(DEFAULT_SAMPLE_SIZE);
    }

    private static List<Map<Class<? extends Annotation>, Annotation>> collectAnnotations(ExtensionContext extensionContext) {
        Map<Class<? extends Annotation>, Annotation> defaultMap =
            Arrays.stream(extensionContext.getRequiredTestClass().getAnnotations())
                  .collect(toMap(Annotation::getClass, a -> a, (a1, b) -> b));
        var testMethod = extensionContext.getRequiredTestMethod();
        defaultMap.putAll(
            Arrays.stream(testMethod.getAnnotations())
                  .collect(toMap(Annotation::getClass, a -> a, (a1, b) -> b)));
        return Arrays.stream(testMethod.getParameterAnnotations())
                     .map(annotations -> Arrays.stream(annotations).collect(
                         toMap(Annotation::getClass, a -> a, (a1, b) -> b, () -> new HashMap<>(defaultMap))))
                     .collect(toList());
    }

    private static List<Map<Class<?>, Class<?>>> collectCustomGenerators(ExtensionContext extensionContext) {
        Map<Class<?>, Class<?>> defaultMap =
            Arrays.stream(extensionContext.getRequiredTestClass().getAnnotationsByType(Custom.class))
                  .collect(toMap(Custom::target, Custom::generator));
        var testMethod = extensionContext.getRequiredTestMethod();
        defaultMap.putAll(
            Arrays.stream(testMethod.getAnnotationsByType(Custom.class))
                  .collect(toMap(Custom::target, Custom::generator)));
        return Arrays.stream(testMethod.getParameterAnnotations())
                     .map(annotations -> Stream.concat(
                         Arrays.stream(annotations)
                               .filter(Custom.class::isInstance)
                               .map(Custom.class::cast),
                         Arrays.stream(annotations)
                               .filter(Customs.class::isInstance)
                               .map(Customs.class::cast)
                               .flatMap(cs -> Arrays.stream(cs.value())))
                                               .collect(toMap(
                                                   Custom::target,
                                                   Custom::generator,
                                                   (a1, b) -> b,
                                                   () -> new HashMap<>(defaultMap))))
                     .collect(toList());

    }

}
