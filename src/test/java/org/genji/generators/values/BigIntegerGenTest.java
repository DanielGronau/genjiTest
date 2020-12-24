package org.genji.generators.values;

import org.genji.TypeInfo;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.Map;
import java.util.Random;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static org.assertj.core.api.Assertions.assertThat;

class BigIntegerGenTest {

    private static final Random RANDOM = new Random();

    @Test
    void generate() {
        var list = BigIntegerGen.INSTANCE
                       .generate(RANDOM, new TypeInfo(BigInteger.class))
                       .limit(50)
                       .collect(toList());

        assertThat(list.stream().anyMatch(i -> i.compareTo(BigInteger.ZERO) < 0)).isTrue();
        assertThat(list.stream().anyMatch(i -> i.compareTo(BigInteger.ZERO) > 0)).isTrue();
        assertThat(list.stream().anyMatch(i -> i.abs().compareTo(BigInteger.valueOf(10000)) > 0)).isTrue();
    }

    @Test
    @BigIntegerSpec(oneOf = {"2", "5", "808017424794512875886459904961710757005754368000000000"})
    void generate_OneOf() throws Exception {
        var spec = BigIntegerGenTest.class
                       .getDeclaredMethod("generate_OneOf")
                       .getAnnotation(BigIntegerSpec.class);
        var set = BigIntegerGen.INSTANCE
                      .generate(RANDOM, new TypeInfo(BigInteger.class, Map.of(BigIntegerSpec.class, spec)))
                      .limit(50)
                      .collect(toSet());

        assertThat(set).containsExactlyInAnyOrder(BigInteger.TWO, BigInteger.valueOf(5),
            new BigInteger("808017424794512875886459904961710757005754368000000000"));
    }

    @Test
    @BigIntegerSpec(from = "3", to = "15")
    void generate_FromTo() throws Exception {
        var intSpec = BigIntegerGenTest.class
                          .getDeclaredMethod("generate_FromTo")
                          .getAnnotation(BigIntegerSpec.class);
        var list = BigIntegerGen.INSTANCE
                       .generate(RANDOM, new TypeInfo(BigInteger.class, Map.of(BigIntegerSpec.class, intSpec)))
                       .limit(50)
                       .collect(toList());

        assertThat(list.stream().allMatch(i -> i.compareTo(BigInteger.valueOf(3)) >= 0)).isTrue();
        assertThat(list.stream().allMatch(i -> i.compareTo(BigInteger.valueOf(15)) <= 0)).isTrue();
        assertThat(list.stream().anyMatch(i -> i.compareTo(BigInteger.valueOf(5)) >= 0
                                                   && i.compareTo(BigInteger.valueOf(12)) <= 0)).isTrue();
    }

}