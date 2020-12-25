package org.genji.generators.values;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.math.BigInteger;

import static java.lang.annotation.ElementType.*;

/**
 * Specifies the range of {@link org.genji.Generator} values for {@link BigInteger}.
 */
@Target({MODULE, PACKAGE, TYPE, METHOD, PARAMETER, TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
public @interface BigIntegerSpec {
    // overwrites the other values
    String[] oneOf() default {};

    String from() default "-1_000_000_000_000_000_000_000";
    String to() default "1_000_000_000_000_000_000_000";
}
