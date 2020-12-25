package org.genji.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

/**
 * Inserts null values into a {@link org.genji.Generator} with the given probability. Does not affect primitives.
 */
@Target({MODULE, PACKAGE, TYPE, METHOD, PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface WithNulls {
    double probability() default 0.05;
}
