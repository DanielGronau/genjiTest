package org.genji.defaultgenerators;

import org.genji.Generator;
import org.genji.GeneratorResolver;
import org.genji.ReflectionSupport;
import org.genji.TypeInfo;
import org.genji.annotations.OptionalSpec;

import java.util.Optional;
import java.util.Random;
import java.util.stream.Stream;

@OptionalSpec
public class OptionalGen implements Generator<Optional<?>> {

    public static final OptionalGen INSTANCE = new OptionalGen();

    private OptionalGen() {
    }

    @Override
    public Stream<Optional<?>> generate(Random random, TypeInfo typeInfo) {
        OptionalSpec spec = ReflectionSupport.findAnnotation(OptionalSpec.class, typeInfo, OptionalGen.class);
        var typeParameter = typeInfo.getParameterType(0);
        return GeneratorResolver.generatorFor(typeParameter.getType())
                                .generate(random, typeParameter)
                                .map(value -> random.nextDouble() < spec.probabilityForEmpty()
                                        ? Optional.empty()
                                        : Optional.ofNullable(value));
    }
}
