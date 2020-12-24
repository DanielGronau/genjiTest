package org.genji.generators.primitives;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Specifies the range of {@link org.genji.Generator} values for {@link Byte}.
 */
@Target({ElementType.PARAMETER, ElementType.METHOD, ElementType.TYPE, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ByteSpec {
    // overwrites the other values
    byte[] oneOf() default {};

    byte from() default Byte.MIN_VALUE;
    byte to() default Byte.MAX_VALUE;
}
