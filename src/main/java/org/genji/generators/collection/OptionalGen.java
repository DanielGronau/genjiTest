package org.genji.generators.collection;

import org.genji.Generator;
import org.genji.provider.GeneratorResolver;
import org.genji.provider.ReflectionSupport;
import org.genji.TypeInfo;
import org.genji.generators.values.OptionalSpec;

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
