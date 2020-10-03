package org.genji.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Specifies characteristics of {@link String}s.
 */
@Target({ElementType.PARAMETER, ElementType.METHOD, ElementType.TYPE, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
public @interface OptionalSpec {
    //probability to produce an empty Optional, between 0 and 1
    double probabilityForEmpty() default 0.1;
}
