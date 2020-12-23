package org.genji;

import java.util.Random;
import java.util.stream.Stream;

public interface Generator<T> {

    Stream<T> generate(Random random, TypeInfo typeInfo);

}
