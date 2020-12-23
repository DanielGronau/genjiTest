package org.genji.defaultgenerators;

import org.genji.Generator;

public class WithNullGen {

    public static <T> Generator<T> withNulls(double probability, Generator<T> source) {
        return (random, typeInfo) ->
                   source
                       .generate(random, typeInfo)
                       .map(t -> random.nextDouble() < probability ? null : t);
    }
}
