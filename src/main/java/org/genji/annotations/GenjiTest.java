package org.genji.annotations;

import org.genji.GenjiProvider;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.lang.annotation.*;

/**
 * Annotation for generator based testing.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@ArgumentsSource(GenjiProvider.class)
@ParameterizedTest
public @interface GenjiTest {

}
