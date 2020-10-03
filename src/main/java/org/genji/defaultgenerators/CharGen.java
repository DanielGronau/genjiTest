package org.genji.defaultgenerators;

import org.genji.Generator;
import org.genji.annotations.CharSpec;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import static org.genji.Support.findAnnotation;

@CharSpec
public class CharGen implements Generator<Character> {
    @Override
    public Stream<Character> generate(Random random, List<Annotation> annotations, Type... parameterTypes) {
        CharSpec spec = findAnnotation(CharSpec.class, annotations)
                            .orElseGet(() -> CharGen.class.getAnnotation(CharSpec.class));
        return generate(random, spec.charSet());
    }

    public Stream<Character> generate(Random random, String charSet) {
        if (charSet.isEmpty()) {
            throw new IllegalArgumentException("@CharSpec.charSet cannot be empty");
        }
        return random.ints(0, charSet.length()).mapToObj(charSet::charAt);
    }
}
