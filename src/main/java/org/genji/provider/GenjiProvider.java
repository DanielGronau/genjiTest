package org.genji.provider;

import org.genji.Generator;
import org.genji.annotations.Custom;
import org.genji.annotations.Samples;
import org.genji.annotations.Seed;
import org.genji.annotations.WithNulls;
import org.genji.generators.WithNullGen;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static org.genji.provider.ReflectionSupport.*;

@Samples
public class GenjiProvider implements ArgumentsProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(GenjiProvider.class);
    private static final Random RANDOM = new Random();

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
        LOGGER.debug("providing arguments: {}", extensionContext);
        var method = extensionContext.getRequiredTestMethod();
        var annotations = parameterAnnotations(method);
        var sampleSize = getSampleSize(method);
        var generators = getGenerators(method);
        var typeInfos = typeInfos(method);
        var iterators =
            IntStream.range(0, generators.size())
                     .mapToObj(i -> generators
                                        .get(i)
                                        .generate(
                                            getRandom(annotations.get(i)),
                                            typeInfos.get(i))
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
        var customGenerators = collectCustomGenerators(method);
        var withNullsOnMethod = methodAnnotation(method, WithNulls.class);
        return
            IntStream.range(0, method.getParameterCount())
                     .mapToObj(i -> {
                         Parameter parameter = method.getParameters()[i];
                         Map<Class<?>, CustomGeneratorInfo> customMap = customGenerators.get(i);
                         Generator<?> generator = GeneratorResolver.resolve(parameter.getType(), customMap).orElseThrow(
                             () -> new NoGeneratorFoundException("for " + parameter));
                         return parameter.getType().isPrimitive()
                                    ? generator //don't apply WithNulls on primitives
                                    : findAnnotation(WithNulls.class, List.of(method.getParameterAnnotations()[i]))
                                          .or(() -> withNullsOnMethod)
                                          .<Generator<?>>map(
                                              withNulls -> WithNullGen
                                                               .withNulls(withNulls.probability(), generator))
                                          .orElse(generator);
                     })
                     .collect(toList());
    }

    private static int getSampleSize(Method method) {
        return methodAnnotation(method, Samples.class)
                   .map(Samples::value)
                   .filter(v -> v > 0)
                   .orElse(GenjiProvider.class.getAnnotation(Samples.class).value());
    }

    private static List<Map<Class<?>, CustomGeneratorInfo>> collectCustomGenerators(Method testMethod) {
        var defaultMap = repeatableMethodAnnotation(testMethod, Custom.class)
                             .stream()
                             .collect(toMap(Custom::target, CustomGeneratorInfo::new, (a, b) -> b));
        return Arrays.stream(testMethod.getParameterAnnotations())
                     .map(annotations -> filterRepeatableAnnotation(Arrays.asList(annotations), Custom.class)
                                             .stream()
                                             .collect(toMap(
                                                 Custom::target,
                                                 CustomGeneratorInfo::new,
                                                 (a1, b) -> b,
                                                 () -> new HashMap<>(defaultMap))))
                     .collect(toList());

    }

    static class CustomGeneratorInfo {
        final Class<Generator<?>> generatorClass;
        final String[] arguments;

        @SuppressWarnings("unchecked")
        private CustomGeneratorInfo(Custom customAnnotation) {
            this((Class<Generator<?>>) customAnnotation.generator(), customAnnotation.arguments());
        }

        private CustomGeneratorInfo(Class<Generator<?>> generatorClass, String[] arguments) {
            this.generatorClass = generatorClass;
            this.arguments = arguments;
        }
    }

}
