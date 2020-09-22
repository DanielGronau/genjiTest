package org.genji.annotations;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Specifies the range of {@link org.genji.Generator} values for an integral number type like {@link Integer}.
 *
 * Can be specified at class or method level.
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Samples {
    int value();
}
