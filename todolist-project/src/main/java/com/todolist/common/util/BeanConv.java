package com.todolist.common.util;

import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Object Conversion Utility
 *
 * @author TodoList Team
 * @since 1.0.0
 */
public class BeanConv {

    /**
     * Convert single object
     *
     * @param source      Source object
     * @param targetClass Target class
     * @param <S>         Source type
     * @param <T>         Target type
     * @return Target object
     */
    public static <S, T> T convert(S source, Class<T> targetClass) {
        if (source == null) {
            return null;
        }
        try {
            T target = targetClass.getDeclaredConstructor().newInstance();
            BeanUtils.copyProperties(source, target);
            return target;
        } catch (Exception e) {
            throw new RuntimeException("Object conversion failed", e);
        }
    }

    /**
     * Convert list of objects
     *
     * @param sourceList  Source list
     * @param targetClass Target class
     * @param <S>         Source type
     * @param <T>         Target type
     * @return Target list
     */
    public static <S, T> List<T> convertList(List<S> sourceList, Class<T> targetClass) {
        if (sourceList == null || sourceList.isEmpty()) {
            return new ArrayList<>();
        }
        return sourceList.stream()
                .map(source -> convert(source, targetClass))
                .collect(Collectors.toList());
    }
}
