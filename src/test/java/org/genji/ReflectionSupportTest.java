package org.genji;

import org.genji.annotations.CharSpec;
import org.genji.annotations.DoubleSpec;
import org.genji.annotations.FloatSpec;
import org.genji.annotations.IntSpec;
import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.assertj.core.api.Assertions.assertThat;

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

        assertThat(ReflectionSupport.findAnnotation(Test.class, annotations)).isPresent();
        assertThat(ReflectionSupport.findAnnotation(CharSpec.class, annotations).isEmpty());

        assertThat(ReflectionSupport.findAnnotation(Test.class, annotations, ReflectionSupportTest.class)).isNotNull();
        assertThat(ReflectionSupport.findAnnotation(CharSpec.class, annotations, ReflectionSupportTest.class)).isNotNull();
        assertThat(ReflectionSupport.findAnnotation(IntSpec.class, annotations, ReflectionSupportTest.class)).isNull();
    }

    @Test
    void getRawType() throws Exception {
        var type = ReflectionSupportTest.class.getDeclaredField("value").getGenericType();
        assertThat(ReflectionSupport.getRawType(type)).isEqualTo(Map.class);

        assertThat(ReflectionSupport.getRawType(Integer.class)).isEqualTo(Integer.class);
        assertThat(ReflectionSupport.getRawType(Integer.TYPE)).isEqualTo(Integer.class);
    }

    @Test
    void getParameterTypes() throws Exception {
        var type = ReflectionSupportTest.class.getDeclaredField("value").getGenericType();
        var parameterTypes = ReflectionSupport.getParameterTypes(type);

        assertThat(parameterTypes[0]).isInstanceOf(ParameterizedType.class);
        assertThat(((ParameterizedType) parameterTypes[0]).getRawType()).isEqualTo(Optional.class);

        assertThat(parameterTypes[1])
            .isInstanceOf(Class.class)
            .isEqualTo(Date.class);
    }

    @Test
    void construct() {
        var empty = ReflectionSupport.construct(String.class);
        assertThat(empty).contains("");
        var string = ReflectionSupport.construct(String.class, (Object) new char[]{'h', 'i'});
        assertThat(string).contains("hi");
        var nope = ReflectionSupport.construct(String.class, new Thread(() -> {}));
        assertThat(nope).isEmpty();
    }

    @Test
    void superTypes() {
        var superLong = StreamSupport.stream(ReflectionSupport.superTypes(long.class).spliterator(), false).collect(Collectors.toSet());
        assertThat(superLong).containsExactlyInAnyOrder(Long.class, Number.class, Object.class, Serializable.class, Comparable.class);
    }

    @Test
    void boxIfNecessary() {
        assertThat(ReflectionSupport.boxIfNecessary(String.class)).isEqualTo(String.class);
        assertThat(ReflectionSupport.boxIfNecessary(Integer.class)).isEqualTo(Integer.class);
        assertThat(ReflectionSupport.boxIfNecessary(int.class)).isEqualTo(Integer.class);
    }
}