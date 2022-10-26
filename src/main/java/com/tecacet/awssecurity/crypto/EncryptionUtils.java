package com.tecacet.awssecurity.crypto;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class EncryptionUtils {

    //TODO: refactor into component with caching
    public static List<Field> getAnnotatedFields(Class<?> type) {

        return Arrays.stream(type.getDeclaredFields())
                .filter(field -> field.getAnnotationsByType(Encrypted.class).length > 0)
                .collect(Collectors.toList());
    }
}
