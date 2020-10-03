package org.genji.defaultgenerators;

import org.genji.Generator;
import org.genji.annotations.StringSpec;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import static org.genji.Support.findAnnotation;

@StringSpec
public class StringGen implements Generator<String> {

    @Override
    public Stream<String> generate(Random random, List<Annotation> annotations, Type... types) {
        StringSpec spec = findAnnotation(StringSpec.class, annotations)
                              .orElseGet(() -> StringGen.class.getAnnotation(StringSpec.class));

        int lengthFrom = (int) Math.max(spec.from(), 0);
        int lengthTo = (int) Math.max(spec.to(), lengthFrom);
        String chars = spec.charSet();
        String[] oneOf = spec.oneOf();

        if (oneOf.length == 0 && chars.isEmpty()) {
            throw new IllegalArgumentException("@StringSpec.charSet cannot be empty");
        }

        return oneOf.length == 0
                   ? generateSynthetic(random, lengthFrom, lengthTo, chars)
                   : Stream.generate(() -> oneOf[random.nextInt(oneOf.length)]);
    }

    private static Stream<String> generateSynthetic(Random random, int lengthFrom, int lengthTo, String chars) {
        var charIt = new CharGen().generate(random, chars).iterator();
        var sb = new StringBuilder();
        return Stream.generate(() -> {
            sb.setLength(0);
            int length = random.nextInt(lengthTo - lengthFrom) + lengthFrom;
            for (int i = 0; i < length; i++) {
                sb.append(charIt.next());
            }
            return sb.toString();
        });
    }

}
