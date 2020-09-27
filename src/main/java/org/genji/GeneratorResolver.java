package org.genji;

import org.genji.defaultgenerators.EnumGen;
import org.genji.defaultgenerators.IntegerGen;
import org.genji.defaultgenerators.ListGen;
import org.genji.defaultgenerators.StringGen;

import java.util.*;

public final class GeneratorResolver {

    private static final Map<Class<?>, Generator<?>> GENERATORS = new HashMap<>();

    private static final Map<Class<?>, Class<?>> SUPER_GENERATORS = new HashMap<>();

    static {
        GENERATORS.put(Integer.class, new IntegerGen());
        GENERATORS.put(String.class, new StringGen());
        GENERATORS.put(List.class, new ListGen());

        SUPER_GENERATORS.put(Enum.class, EnumGen.class);
    }

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
