package com.tecacet.awssecurity.crypto;

import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class AnnotatedFieldProvider {

    private final Map<Class<?>, Map<Class<? extends Annotation>, List<Field>>> cache = new HashMap<>();

    public List<Field> getAnnotatedFields(Class<?> type, Class<? extends Annotation> annotation) {
        return get(type, annotation)
                .orElseGet(() -> collectAndCache(type, annotation));
    }

    private List<Field> collectAndCache(Class<?> type, Class<? extends Annotation> annotation) {
        List<Field> fields = collectAnnotations(type, annotation);
        put(type, annotation, fields);
        return fields;
    }

    private List<Field> collectAnnotations(Class<?> type, Class<? extends Annotation> annotation) {
        return Arrays.stream(type.getDeclaredFields())
                .filter(field -> field.getAnnotationsByType(annotation).length > 0)
                .collect(Collectors.toList());
    }

    private Optional<List<Field>> get(Class<?> type, Class<? extends Annotation> annotation) {
        Map<Class<? extends Annotation>, List<Field>> map = cache.get(type);
        if (map == null) {
            return Optional.empty();
        }
        List<Field> fields = map.get(annotation);
        return fields == null ? Optional.empty() : Optional.of(fields);
    }

    private void put(Class<?> type, Class<? extends Annotation> annotation, List<Field> fields) {
        Map<Class<? extends Annotation>, List<Field>> map = cache.get(type);
        if (map == null) {
            map = new HashMap<>();
        }
        map.put(annotation, fields);
        cache.put(type, map);
    }
}
