package org.genji;

import org.genji.defaultgenerators.*;

import java.util.*;

public final class GeneratorResolver {

    private static final Map<Class<?>, Generator<?>> GENERATORS =
        Map.ofEntries(
            Map.entry(String.class, StringGen.INSTANCE),
            Map.entry(List.class, ListGen.INSTANCE),

            Map.entry(Integer.TYPE, IntGen.INSTANCE),
            Map.entry(Integer.class, IntGen.INSTANCE),

            Map.entry(Character.TYPE, CharGen.INSTANCE),
            Map.entry(Character.class, CharGen.INSTANCE),

            Map.entry(Boolean.TYPE, BoolGen.INSTANCE),
            Map.entry(Boolean.class, BoolGen.INSTANCE)
        );

    private static final Map<Class<?>, Class<?>> SUPER_GENERATORS =
        Map.ofEntries(
            Map.entry(Enum.class, EnumGen.class)
        );

    public static Optional<Generator<?>> resolve(Class<?> rawType) {
        return standardGenerator(rawType).or(
            () -> superGenerator(rawType));
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
                    return Optional.of((Generator<?>) generatorClass
                                                          .getConstructor(Class.class)
                                                          .newInstance(rawType));
                } catch (ReflectiveOperationException e) {
                    throw new AssertionError(
                        "Generator class "
                            + generatorClass.getSimpleName()
                            + " should have a one argument constructor taking the subclass to instantiate");
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
