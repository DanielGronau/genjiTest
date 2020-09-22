package org.genji.defaultgenerators;

import org.genji.Generator;
import org.genji.annotations.Size;
import org.genji.annotations.StringSize;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Stream;

import static org.genji.Support.*;

public class StringGen implements Generator<String> {

    static final String DEFAULT_CHARS = " \0\t\n\r\\\"'²³?!#+*/;.-_<>|§$%&1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ[]{}";
    static final int DEFAULT_LENGTH = 20;

    @Override
    public Stream<String> generate(Random random, List<Annotation> annotations, Type... types) {
        String chars = DEFAULT_CHARS;
        Optional<StringSize> sizeAnnotation = findAnnotation(StringSize.class, annotations);

        int lengthFrom = sizeAnnotation.map(a -> (int) Math.max(a.from(), 0)).orElse(0);
        int lengthTo = sizeAnnotation.map(a -> (int) Math.max(a.to(), lengthFrom)).orElse(DEFAULT_LENGTH);

        StringBuilder sb = new StringBuilder();
        return Stream.generate(() -> {
            sb.setLength(0);
            int length = random.nextInt(lengthTo - lengthFrom) + lengthFrom;
            for (int i = 0; i < length; i++) {
                sb.append(chars.charAt(random.nextInt(chars.length())));
            }
            return sb.toString();
        });
    }

}
