package org.genji.defaultgenerators;

import org.genji.Generator;
import org.genji.Support;
import org.genji.annotations.OptionalSpec;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Stream;

@OptionalSpec
public class OptionalGen implements Generator<Optional<?>> {

    public static final OptionalGen INSTANCE = new OptionalGen();

    private OptionalGen() {
    }

    @Override
    public Stream<Optional<?>> generate(Random random, List<Annotation> annotations, Type... types) {
        OptionalSpec spec = Support.findAnnotation(OptionalSpec.class, annotations, OptionalGen.class);
        return Support.generatorFor(types[0])
                      .generate(random, annotations, Support.getParameterTypes(types[0]))
                      .map(value -> random.nextDouble() < spec.probabilityForEmpty()
                                        ? Optional.empty()
                                        : Optional.ofNullable(value));
    }
}
