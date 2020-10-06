package org.genji.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.math.BigInteger;

/**
 * Specifies the range of {@link org.genji.Generator} values for {@link BigInteger}.
 */
@Target({ElementType.PARAMETER, ElementType.METHOD, ElementType.TYPE, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
public @interface BigIntegerSpec {
    // overwrites the other values
    String[] oneOf() default {};

    String from() default "-1_000_000_000_000_000_000_000";
    String to() default "1_000_000_000_000_000_000_000";
}
