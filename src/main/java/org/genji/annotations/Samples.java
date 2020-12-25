package org.genji.annotations;


import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

/**
 * Specifies the number of samples to be generated.
 * <p>
 * Cannot be specified on parameter or parameter generic level
 * as it refers to the whole argument list of the method.
 */
@Target({MODULE, PACKAGE, TYPE, METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Samples {
    int value() default 20;
}
