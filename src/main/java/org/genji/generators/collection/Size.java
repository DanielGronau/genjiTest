package org.genji.generators.collection;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

/**
 * Specifies the size of {@link org.genji.Generator} values for Java collections etc.
 */
@Target({MODULE, PACKAGE, TYPE, METHOD, PARAMETER, TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Size {
    int from() default 0;
    int to() default 20;
}
