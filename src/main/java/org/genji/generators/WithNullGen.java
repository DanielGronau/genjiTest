package org.genji.generators;

import org.genji.Generator;

public class WithNullGen {

    private WithNullGen() {
        throw new UnsupportedOperationException("do not instantiate");
    }

    public static <T> Generator<T> withNulls(double probability, Generator<T> source) {
        return (random, typeInfo) ->
                   source
                       .generate(random, typeInfo)
                       .map(t -> random.nextDouble() < probability ? null : t);
    }
}
