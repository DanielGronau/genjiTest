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
public @interface ShortSpec {
    // overwrites the other values
    short[] oneOf() default {};

    short from() default Short.MIN_VALUE;
    short to() default Short.MAX_VALUE;
}
