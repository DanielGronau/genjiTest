package org.genji;

import org.genji.annotations.Custom;
import org.genji.defaultgenerators.*;

import java.lang.reflect.Array;
import java.math.BigInteger;
import java.util.*;

import static java.util.Map.entry;
import static java.util.Map.ofEntries;

public final class GeneratorResolver {

    private static final Map<Class<?>, Generator<?>> GENERATORS =
        ofEntries(
            entry(String.class, StringGen.INSTANCE),
            entry(List.class, ListGen.INSTANCE),
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
            entry(Enum.class, EnumGen.class)
        );

    public static Optional<Generator<?>> resolve(Class<?> rawType, Map<Class<?>, Class<?>> customGenerators) {
        return
            customGenerator(rawType, customGenerators)
                .or(() -> standardGenerator(rawType))
                .or(() -> superGenerator(rawType));
    }

    private static Optional<Generator<?>> customGenerator(Class<?> type, Map<Class<?>, Class<?>> customGenerators) {
        Class<?> rawType = type;
        if (rawType.isPrimitive()) {
            rawType = Array.get(Array.newInstance(rawType,1),0).getClass();
        }

        List<Class<?>> todo = new ArrayList<>();
        todo.add(rawType);
        while (!todo.isEmpty()) {
            Class<?> superClassOrInterface = todo.remove(0);
            Class<?> generatorClass = customGenerators.get(superClassOrInterface);
            if (generatorClass != null) {
                try {
                    return Optional.of(
                        (Generator<?>) generatorClass.getConstructor(Class.class).newInstance(rawType));
                } catch (ReflectiveOperationException e) {
                    try {
                        return Optional.of(
                            (Generator<?>) generatorClass.getConstructor().newInstance());
                    } catch (ReflectiveOperationException f) {
                        throw new AssertionError(
                            "Generator class "
                                + generatorClass.getSimpleName()
                                + " should have either a zero argument or a one argument constructor taking the subclass to instantiate");
                    }
                }
            }
            if (superClassOrInterface.getSuperclass() != null) {
                todo.add(superClassOrInterface.getSuperclass());
            }
            todo.addAll(Arrays.asList(superClassOrInterface.getInterfaces()));
        }
        return Optional.empty();

    }

    private static Optional<Generator<?>> standardGenerator(Class<?> rawType) {
        return Optional.ofNullable(GENERATORS.get(rawType));
    }

    private static Optional<Generator<?>> superGenerator(Class<?> rawType) {
        List<Class<?>> todo = new ArrayList<>();
        todo.add(rawType);
        while (!todo.isEmpty()) {
            Class<?> superClassOrInterface = todo.remove(0);
            Class<?> generatorClass = SUPER_GENERATORS.get(superClassOrInterface);
            if (generatorClass != null) {
                try {
                    return Optional.of(
                        (Generator<?>) generatorClass.getConstructor(Class.class).newInstance(rawType));
                } catch (ReflectiveOperationException e) {
                    try {
                        return Optional.of(
                            (Generator<?>) generatorClass.getConstructor().newInstance());
                    } catch (ReflectiveOperationException f) {
                        throw new AssertionError(
                            "Generator class "
                                + generatorClass.getSimpleName()
                                + " should have a either a zero argument or a one argument constructor taking the subclass to instantiate");
                    }
                }
            }
            if (superClassOrInterface.getSuperclass() != null) {
                todo.add(superClassOrInterface.getSuperclass());
            }
            todo.addAll(Arrays.asList(superClassOrInterface.getInterfaces()));
        }
        return Optional.empty();
    }

}
