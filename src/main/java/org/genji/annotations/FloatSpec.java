package org.genji.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Specifies the range of {@link org.genji.Generator} values for {@link Float}.
 */
@Target({ElementType.PARAMETER, ElementType.METHOD, ElementType.TYPE, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
public @interface FloatSpec {
    // overwrites the other values
    float[] oneOf() default {};

    float from() default -Float.MAX_VALUE;
    float to() default Float.MAX_VALUE;
}
