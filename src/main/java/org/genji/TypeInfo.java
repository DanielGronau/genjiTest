package org.genji;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;

public class TypeInfo {
    private final Class<?> type;
    private final Map<Class<? extends Annotation>, Annotation> annotations;
    private final List<TypeInfo> parameterTypes;

    public TypeInfo(Class<?> type, Map<Class<? extends Annotation>, Annotation> annotations, List<TypeInfo> children) {
        this.type = type;
        this.annotations = annotations;
        this.parameterTypes = children;
    }

    public TypeInfo(Class<?> type, Map<Class<? extends Annotation>, Annotation> annotations) {
        this(type, annotations, List.of());
    }

    public TypeInfo(Class<?> type) {
        this(type, Map.of());
    }

    public Class<?> getType() {
        return type;
    }

    public Map<Class<? extends Annotation>, Annotation> getAnnotations() {
        return annotations;
    }

    public List<TypeInfo> getParameterTypes() {
        return parameterTypes;
    }

    public TypeInfo getParameterType(int i) {
        return parameterTypes.get(i);
    }
}
