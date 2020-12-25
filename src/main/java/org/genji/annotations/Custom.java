package org.genji.annotations;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

/**
 * Allows to specify a custom generator for a certain target class.
 */
@Repeatable(Customs.class)
@Target({MODULE, PACKAGE, TYPE, METHOD, PARAMETER, TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Custom {
    Class<?> target();
    Class<?> generator();
    String[] arguments() default {};
}
