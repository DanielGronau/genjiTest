package org.genji.generators.primitives;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

/**
 * Specifies the range of {@link org.genji.Generator} values for {@link Integer}.
 */
@Target({MODULE, PACKAGE, TYPE, METHOD, PARAMETER, TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
public @interface IntSpec {
    // overwrites the other values
    int[] oneOf() default {};

    int from() default Integer.MIN_VALUE;
    int to() default Integer.MAX_VALUE;
}
