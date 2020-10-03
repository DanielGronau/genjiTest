package org.genji.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Specifies the range of {@link org.genji.Generator} values for {@link Long}.
 */
@Target({ElementType.PARAMETER, ElementType.METHOD, ElementType.TYPE, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
public @interface LongSpec {
    // overwrites the other values
    long[] oneOf() default {};

    long from() default Long.MIN_VALUE;
    long to() default Long.MAX_VALUE;
}
