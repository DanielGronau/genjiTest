package org.genji.annotations;

import java.lang.annotation.*;

/**
 *
 */
@Repeatable(Customs.class)
@Target({ElementType.PARAMETER, ElementType.METHOD, ElementType.TYPE, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Custom {
    Class<?> target();
    Class<?> generator();
    String[] arguments() default {};
}
