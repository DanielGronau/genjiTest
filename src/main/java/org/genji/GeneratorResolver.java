package org.genji;

import org.genji.defaultgenerators.IntegerGen;
import org.genji.defaultgenerators.ListGen;
import org.genji.defaultgenerators.StringGen;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public final class GeneratorResolver {

    private static final Map<Class<?>, Generator<?>> GENERATORS = new HashMap<>();

    static {
        GENERATORS.put(Integer.class, new IntegerGen());
        GENERATORS.put(String.class, new StringGen());
        GENERATORS.put(List.class, new ListGen());
    }

    public static Optional<? extends Generator<?>> resolve(Class<?> rawType) {
        return Optional.ofNullable(GENERATORS.get(rawType));
    }

}
