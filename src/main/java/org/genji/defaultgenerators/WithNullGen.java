package org.genji.defaultgenerators;

import org.genji.Generator;

public class WithNullGen {

    public static <T> Generator<T> withNulls(double probability, Generator<T> source) {
        return (random, annotations, types) ->
                   source
                       .generate(random, annotations, types)
                       .map(t -> random.nextDouble() < probability ? null : t);
    }
}
