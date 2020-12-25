package org.genji.generators.primitives;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

/**
 * Specifies the range of {@link org.genji.Generator} values for {@link Long}.
 */
@Target({MODULE, PACKAGE, TYPE, METHOD, PARAMETER, TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
public @interface LongSpec {
    // overwrites the other values
    long[] oneOf() default {};

    long from() default Long.MIN_VALUE;
    long to() default Long.MAX_VALUE;
}
