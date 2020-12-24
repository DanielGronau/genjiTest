package org.genji.generators.primitives;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Specifies the range of {@link org.genji.Generator} values for {@link Integer}.
 */
@Target({ElementType.PARAMETER, ElementType.METHOD, ElementType.TYPE, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
public @interface IntSpec {
    // overwrites the other values
    int[] oneOf() default {};

    int from() default Integer.MIN_VALUE;
    int to() default Integer.MAX_VALUE;
}
