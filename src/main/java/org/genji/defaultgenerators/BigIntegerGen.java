package org.genji.defaultgenerators;

import org.genji.Generator;
import org.genji.annotations.BigIntegerSpec;
import org.genji.annotations.IntSpec;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import static org.genji.Support.findAnnotation;

@BigIntegerSpec
public class BigIntegerGen implements Generator<BigInteger> {

    public static final BigIntegerGen INSTANCE = new BigIntegerGen();

    private BigIntegerGen() {
    }

    @Override
    public Stream<BigInteger> generate(Random random, List<Annotation> annotations, Type... parameterTypes) {
        BigIntegerSpec bigIntegerSpec = findAnnotation(BigIntegerSpec.class, annotations, BigIntegerGen.class);
        BigInteger from = new BigInteger(bigIntegerSpec.from().replaceAll("_",""));
        BigInteger to = new BigInteger(bigIntegerSpec.to().replaceAll("_",""));
        if (from.compareTo(to) > 0) {
            throw new IllegalArgumentException("Invalid BigInteger range, from (" + from + ") > to(" + to + ")");
        }
        BigInteger[] oneOf = Arrays.stream(bigIntegerSpec.oneOf())
                                   .map(s -> s.replaceAll("_",""))
                                   .map(BigInteger::new).toArray(BigInteger[]::new);
        return oneOf.length == 0
                   ? Stream.generate(() -> generateBetween(random, from, to))
                   : random.ints(0, oneOf.length).mapToObj(i -> oneOf[i]);
    }

    private static BigInteger generateBetween(Random random, BigInteger from, BigInteger to) {
        BigInteger bigInteger = to.subtract(from);
        int len = to.bitLength();
        return new BigInteger(len, random).mod(bigInteger.add(BigInteger.ONE)).add(from);
    }

}
