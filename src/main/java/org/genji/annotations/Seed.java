package org.genji.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Uses a seed for the random number generator, hence the values from an affected
 * {@link org.genji.Generator} will be deterministic.
 *
 */
@Target({ElementType.PARAMETER, ElementType.METHOD, ElementType.TYPE, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Seed {
    long value();
}
