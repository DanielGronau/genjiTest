package org.genji;

import org.genji.annotations.Samples;
import org.genji.annotations.Seed;
import org.genji.annotations.WithNulls;
import org.genji.defaultgenerators.WithNullGen;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static org.genji.Support.findAnnotation;
import static org.genji.Support.getParameterTypes;

public class GenjiProvider implements ArgumentsProvider {

    private static final int DEFAULT_SAMPLE_SIZE = 20;
    private static final Random RANDOM = new Random();

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
        var annotations = collectAnnotations(extensionContext);
        var sampleSize = getSampleSize(extensionContext);
        var generators = getGenerators(extensionContext.getRequiredTestMethod());
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

    private static List<? extends Generator<?>> getGenerators(Method method) {
        List<Generator<?>> generators =
            Arrays.stream(method.getParameters())
                  .map(parameter -> GeneratorResolver.resolve(parameter.getType()).orElseThrow(
                      () -> new NoGeneratorFoundException("for " + parameter)))
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

}
