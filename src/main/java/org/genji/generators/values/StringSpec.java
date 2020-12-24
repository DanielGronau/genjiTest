package org.genji.generators.values;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Specifies characteristics of {@link String}s.
 */
@Target({ElementType.PARAMETER, ElementType.METHOD, ElementType.TYPE, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
public @interface StringSpec {
    //overwrites the other properties
    String[] oneOf() default {};

    //cannot be empty if oneOf is not set
    String charSet() default " \0\t\n\r\\\"'²³?!#+*/;.-_<>|§$%&1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ[]{}";

    long from() default 0;
    long to() default 20;
}
