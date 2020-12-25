package org.genji.generators.collection;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

/**
 * Specifies characteristics of {@link String}s.
 */
@Target({MODULE, PACKAGE, TYPE, METHOD, PARAMETER, TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
public @interface OptionalSpec {
    //probability to produce an empty Optional, between 0 and 1
    double probabilityForEmpty() default 0.1;
}
