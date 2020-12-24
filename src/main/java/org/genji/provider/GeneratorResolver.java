package org.genji.provider;

import org.genji.Generator;
import org.genji.generators.collection.ListGen;
import org.genji.generators.collection.OptionalGen;
import org.genji.generators.primitives.*;
import org.genji.generators.values.BigIntegerGen;
import org.genji.generators.values.EnumGen;
import org.genji.generators.values.StringGen;

import java.lang.reflect.Type;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.Map.entry;
import static java.util.Map.ofEntries;
import static org.genji.provider.ReflectionSupport.construct;
import static org.genji.provider.ReflectionSupport.getRawType;

public final class GeneratorResolver {

    private static final Map<Class<?>, Generator<?>> GENERATORS =
        ofEntries(
            entry(String.class, StringGen.INSTANCE),
            entry(Optional.class, OptionalGen.INSTANCE),
            entry(BigInteger.class, BigIntegerGen.INSTANCE),

            entry(Double.TYPE, DoubleGen.INSTANCE),
            entry(Double.class, DoubleGen.INSTANCE),

            entry(Float.TYPE, FloatGen.INSTANCE),
            entry(Float.class, FloatGen.INSTANCE),

            entry(Byte.TYPE, ByteGen.INSTANCE),
            entry(Byte.class, ByteGen.INSTANCE),

            entry(Short.TYPE, ShortGen.INSTANCE),
            entry(Short.class, ShortGen.INSTANCE),

            entry(Integer.TYPE, IntGen.INSTANCE),
            entry(Integer.class, IntGen.INSTANCE),

            entry(Long.TYPE, LongGen.INSTANCE),
            entry(Long.class, LongGen.INSTANCE),

            entry(Character.TYPE, CharGen.INSTANCE),
            entry(Character.class, CharGen.INSTANCE),

            entry(Boolean.TYPE, BoolGen.INSTANCE),
            entry(Boolean.class, BoolGen.INSTANCE)
        );

    private static final Map<Class<?>, Class<?>> SUPER_GENERATORS =
        ofEntries(
            entry(Enum.class, EnumGen.class),
            entry(List.class, ListGen.class)
        );

    public static Optional<Generator<?>> resolve(Class<?> rawType, Map<Class<?>, Class<?>> customGenerators) {
        return
            customGenerator(rawType, customGenerators)
                .or(() -> standardGenerator(rawType))
                .or(() -> superGenerator(rawType));
    }

    @SuppressWarnings("unchecked")
    private static Optional<Generator<?>> customGenerator(Class<?> type, Map<Class<?>, Class<?>> customGenerators) {
        for (Class<?> superClassOrInterface : ReflectionSupport.superTypes(type)) {
            Class<Generator<?>> generatorClass = (Class<Generator<?>>) customGenerators.get(superClassOrInterface);
            if (generatorClass != null) {
                return Optional.of(
                    construct(generatorClass, type)
                        .or(() -> construct(generatorClass))
                        .orElseThrow(() -> noConstructorAssertionError(generatorClass)));
            }
        }
        return Optional.empty();

    }

    private static Optional<Generator<?>> standardGenerator(Class<?> rawType) {
        return Optional.ofNullable(GENERATORS.get(rawType));
    }

    @SuppressWarnings("unchecked")
    private static Optional<Generator<?>> superGenerator(Class<?> rawType) {
        for (Class<?> superClassOrInterface : ReflectionSupport.superTypes(rawType)) {
            Class<Generator<?>> generatorClass = (Class<Generator<?>>) SUPER_GENERATORS.get(superClassOrInterface);
            if (generatorClass != null) {
                return Optional.of(
                    construct(generatorClass, rawType)
                        .or(() -> construct(generatorClass))
                        .orElseThrow(() -> noConstructorAssertionError(generatorClass)));
            }
        }
        return Optional.empty();
    }

    private static AssertionError noConstructorAssertionError(Class<Generator<?>> generatorClass) {
        return new AssertionError(
            "Generator class "
                + generatorClass.getSimpleName()
                + " should have a either a zero argument or a one argument constructor taking the subclass to instantiate"
        );
    }

    public static Generator<?> generatorFor(Type type) throws NoGeneratorFoundException {
        return GeneratorResolver
                   .resolve(getRawType(type), Map.of())
                   .orElseThrow(() -> new NoGeneratorFoundException("for type " + type.toString()));
    }

}
