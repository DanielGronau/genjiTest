package org.genji.defaultgenerators;

import org.genji.annotations.BigIntegerSpec;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.List;
import java.util.Random;
import java.util.Set;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BigIntegerGenTest {

    private static final Random RANDOM = new Random();

    @Test
    void generate() {
        var list = BigIntegerGen.INSTANCE
                       .generate(RANDOM, List.of())
                       .limit(50)
                       .collect(toList());

        assertTrue(list.stream().anyMatch(i -> i.compareTo(BigInteger.ZERO) < 0));
        assertTrue(list.stream().anyMatch(i -> i.compareTo(BigInteger.ZERO) > 0));
        assertTrue(list.stream().anyMatch(i -> i.abs().compareTo(BigInteger.valueOf(10000)) > 0));
    }

    @Test
    @BigIntegerSpec(oneOf = {"2", "5", "808017424794512875886459904961710757005754368000000000"})
    void generate_OneOf() throws Exception {
        var spec = BigIntegerGenTest.class
                             .getDeclaredMethod("generate_OneOf")
                             .getAnnotation(BigIntegerSpec.class);
        var set = BigIntegerGen.INSTANCE
                       .generate(RANDOM, List.of(spec))
                       .limit(50)
                       .collect(toSet());

        assertEquals(Set.of(BigInteger.TWO, BigInteger.valueOf(5),
            new BigInteger("808017424794512875886459904961710757005754368000000000")), set);
    }

    @Test
    @BigIntegerSpec(from = "3", to = "15")
    void generate_FromTo() throws Exception {
        var intSpec = BigIntegerGenTest.class
                          .getDeclaredMethod("generate_FromTo")
                          .getAnnotation(BigIntegerSpec.class);
        var list = BigIntegerGen.INSTANCE
                      .generate(RANDOM, List.of(intSpec))
                      .limit(50)
                      .collect(toList());

        assertTrue(list.stream().allMatch(i -> i.compareTo(BigInteger.valueOf(3)) >= 0));
        assertTrue(list.stream().allMatch(i -> i.compareTo(BigInteger.valueOf(15)) <= 0));
        assertTrue(list.stream().anyMatch(i -> i.compareTo(BigInteger.valueOf(5)) >= 0
                                                   && i.compareTo(BigInteger.valueOf(12)) <= 0));
    }

}