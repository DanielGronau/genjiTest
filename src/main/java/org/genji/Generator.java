package org.genji;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

public interface Generator<T> {

    Stream<T> generate(Random random, List<Annotation> annotations, Type... parameterTypes);

}
