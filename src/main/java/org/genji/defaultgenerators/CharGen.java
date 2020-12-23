package org.genji.defaultgenerators;

import org.genji.Generator;
import org.genji.TypeInfo;
import org.genji.annotations.CharSpec;

import java.util.Random;
import java.util.stream.Stream;

import static org.genji.ReflectionSupport.findAnnotation;

@CharSpec
public class CharGen implements Generator<Character> {

    public static final CharGen INSTANCE = new CharGen();

    private CharGen() {
    }

    @Override
    public Stream<Character> generate(Random random, TypeInfo typeInfo) {
        CharSpec spec = findAnnotation(CharSpec.class, typeInfo, CharGen.class);
        return generate(random, spec.charSet());
    }

    public Stream<Character> generate(Random random, String charSet) {
        if (charSet.isEmpty()) {
            throw new IllegalArgumentException("@CharSpec.charSet cannot be empty");
        }
        return random.ints(0, charSet.length()).mapToObj(charSet::charAt);
    }
}
