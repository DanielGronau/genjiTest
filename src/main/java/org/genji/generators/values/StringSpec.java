package org.genji.generators.values;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

/**
 * Specifies characteristics of {@link String}s.
 */
@Target({MODULE, PACKAGE, TYPE, METHOD, PARAMETER, TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
public @interface StringSpec {
    //overwrites the other properties
    String[] oneOf() default {};

    //cannot be empty if oneOf is not set
    String charSet() default " \0\t\n\r\\\"'²³?!#+*/;.-_<>|§$%&1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ[]{}";

    long from() default 0;
    long to() default 20;
}
