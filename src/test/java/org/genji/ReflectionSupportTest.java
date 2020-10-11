package org.genji;

import org.genji.annotations.CharSpec;
import org.genji.annotations.DoubleSpec;
import org.genji.annotations.FloatSpec;
import org.genji.annotations.IntSpec;
import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.*;

@CharSpec()
class ReflectionSupportTest {

    Map<Optional<String>, Date> value;

    @Test
    @FloatSpec
    @DoubleSpec
    void findAnnotation() throws Exception {
        var annotations = Arrays.asList(
            ReflectionSupportTest.class
                .getDeclaredMethod("findAnnotation")
                .getDeclaredAnnotations());

        assertTrue(ReflectionSupport.findAnnotation(Test.class, annotations).isPresent());
        assertTrue(ReflectionSupport.findAnnotation(CharSpec.class, annotations).isEmpty());

        assertNotNull(ReflectionSupport.findAnnotation(Test.class, annotations, ReflectionSupportTest.class));
        assertNotNull(ReflectionSupport.findAnnotation(CharSpec.class, annotations, ReflectionSupportTest.class));
        assertNull(ReflectionSupport.findAnnotation(IntSpec.class, annotations, ReflectionSupportTest.class));
    }

    @Test
    void getRawType() throws Exception {
        var type = ReflectionSupportTest.class.getDeclaredField("value").getGenericType();
        assertEquals(Map.class, ReflectionSupport.getRawType(type));

        assertEquals(Integer.class, ReflectionSupport.getRawType(Integer.class));
        assertEquals(Integer.class, ReflectionSupport.getRawType(Integer.TYPE));
    }

    @Test
    void getParameterTypes() throws Exception {
        var type = ReflectionSupportTest.class.getDeclaredField("value").getGenericType();
        var parameterTypes = ReflectionSupport.getParameterTypes(type);

        assertTrue(parameterTypes[0] instanceof ParameterizedType);
        assertEquals(Optional.class, ((ParameterizedType) parameterTypes[0]).getRawType());

        assertTrue(parameterTypes[1] instanceof Class);
        assertEquals(Date.class, parameterTypes[1]);
    }

    @Test
    void construct() {
        var empty = ReflectionSupport.construct(String.class).get();
        assertEquals("", empty);
        var string = ReflectionSupport.construct(String.class, new char[]{'h', 'i'}).get();
        assertEquals("hi", string);
        var nope = ReflectionSupport.construct(String.class, new Thread());
        assertTrue(nope.isEmpty());
    }

    @Test
    void superTypes() {
        var superLong = StreamSupport.stream(ReflectionSupport.superTypes(long.class).spliterator(), false).collect(Collectors.toSet());
        assertEquals(Set.of(Long.class, Number.class, Object.class, Serializable.class, Comparable.class), superLong);
    }

    @Test
    void boxIfNecessary() {
        assertEquals(String.class, ReflectionSupport.boxIfNecessary(String.class));
        assertEquals(Integer.class, ReflectionSupport.boxIfNecessary(Integer.class));
        assertEquals(Integer.class, ReflectionSupport.boxIfNecessary(int.class));
    }
}