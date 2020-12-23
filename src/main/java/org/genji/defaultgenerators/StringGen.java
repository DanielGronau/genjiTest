package org.genji.defaultgenerators;

import org.genji.Generator;
import org.genji.TypeInfo;
import org.genji.annotations.StringSpec;

import java.util.Random;
import java.util.stream.Stream;

import static org.genji.ReflectionSupport.findAnnotation;

@StringSpec
public class StringGen implements Generator<String> {

    public static final StringGen INSTANCE = new StringGen();

    private StringGen() {
    }

    @Override
    public Stream<String> generate(Random random, TypeInfo typeInfo) {
        StringSpec spec = findAnnotation(StringSpec.class, typeInfo, StringGen.class);

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
        var charIt = CharGen.INSTANCE.generate(random, chars).iterator();
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
