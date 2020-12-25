package org.genji.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

/**
 * Uses a seed for the random number generator, hence the values from an affected
 * {@link org.genji.Generator} will be deterministic.
 *
 */
@Target({MODULE, PACKAGE, TYPE, METHOD, PARAMETER, TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Seed {
    long value();
}
