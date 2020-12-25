package org.genji.generators.primitives;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

/**
 * Specifies the range of {@link org.genji.Generator} values for {@link Double}.
 */
@Target({MODULE, PACKAGE, TYPE, METHOD, PARAMETER, TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
public @interface DoubleSpec {
    // overwrites the other values
    double[] oneOf() default {};

    //using -MAX_VALUE ... MAX_VALUE would result in a Stream containing only MAX_VALUE values due to overflows
    double from() default -Double.MAX_VALUE/2.0;
    double to() default Double.MAX_VALUE/2.0;
}
