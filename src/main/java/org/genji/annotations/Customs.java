package org.genji.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

/**
 * The wrapper annotation for {@link Custom}.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({MODULE, PACKAGE, TYPE, METHOD, PARAMETER, TYPE_USE})
public @interface Customs {
    Custom[] value();
}
